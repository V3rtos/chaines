package me.moonways.bridgenet.api.modern_x2_command;

import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanMethod;
import me.moonways.bridgenet.api.inject.decorator.EnableDecorators;
import me.moonways.bridgenet.api.inject.decorator.persistence.Async;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessorResult;
import me.moonways.bridgenet.api.inject.processor.persistence.GetTypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.processor.persistence.WaitTypeAnnotationProcessor;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateManagement;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateResult;
import me.moonways.bridgenet.api.modern_x2_command.entity.ConsoleCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.event.CommandExecuteEvent;
import me.moonways.bridgenet.api.modern_x2_command.event.CommandNotFoundEvent;
import me.moonways.bridgenet.api.modern_x2_command.label.CommandLabelContext;
import me.moonways.bridgenet.api.modern_x2_command.registration.CommandRegistrationService;

import java.util.List;
import java.util.Optional;

@WaitTypeAnnotationProcessor(InjectCommand.class)
@Autobind
@EnableDecorators
public class CommandService {

    @Inject
    private EventService eventService;

    @Inject
    private CommandSearchStrategy searchStrategy;
    @Inject
    private AICommandValidateManagement validateManagement;

    @Inject
    private CommandRegistrationService registrationService;

    @GetTypeAnnotationProcessor
    private TypeAnnotationProcessorResult<Object> commandsResult;

    @PostConstruct
    private void registerAll() {
        List<Bean> beansList = commandsResult.toBeansList();
        beansList.forEach(bean -> registrationService.register(bean));
    }

    public void register(Class<?> cls, Object parent) {
        registrationService.register(cls, parent);
    }

    public void register(Bean bean) {
        registrationService.register(bean);
    }

    public synchronized void handle(EntityCommandSender entity, String label) {
        CommandLabelContext labelContext = CommandLabelContext.create(label);

        Optional<Command> command = searchStrategy.search(labelContext);

        if (command.isPresent()) {
            handleOk(entity, command.get(), labelContext);
        } else {
            handleFail(entity);
        }
    }

    @Async
    public void handleAsync(EntityCommandSender entity, String label) {
        this.handle(entity, label);
    }

    public void handleConsole(String label) {
        this.handle(ConsoleCommandSender.INSTANE, label);
    }

    public void unregisterAll() {
        registrationService.unregisterAll();
    }

    public void unregister(Class<?> cls) {
        registrationService.unregister(cls);
    }

    private synchronized void handleOk(EntityCommandSender entity, Command command, CommandLabelContext labelContext) {
        ExecutionContext executionContext = ExecutionContext.create(entity, labelContext);

        if (!validateEvent(executionContext)) return;

        AICommandValidateResult primaryResult = validateManagement.validate(executionContext, command);
        if (primaryResult.isOk()) {
            execute(executionContext, command);
        }
    }

    private synchronized void handleFail(EntityCommandSender entity) {
        CommandNotFoundEvent commandNotFoundEvent = eventService
                .fireEvent(new CommandNotFoundEvent())
                .getFollower()
                .getCompleted();

        if (commandNotFoundEvent.getMessage() != null) {
            entity.sendMessage(commandNotFoundEvent.getMessage());
        }
    }

    private synchronized boolean validateEvent(ExecutionContext executionContext) {
        CommandExecuteEvent executeEvent = eventService
                .fireEvent(new CommandExecuteEvent(executionContext))
                .getFollower()
                .getCompleted();

        return !executeEvent.isCancelled();
    }

    private synchronized void execute(ExecutionContext executionContext, Command command) {
        BeanMethod beanMethod = command.getBeanMethod();

        beanMethod.invoke(executionContext);
    }
}
