package me.moonways.bridgenet.api.modern_x2_command.process.service;

import me.moonways.bridgenet.api.event.Event;
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
import me.moonways.bridgenet.api.modern_x2_command.InjectCommand;
import me.moonways.bridgenet.api.modern_x2_command.annotation.validate.AnnotationCommandValidateManagement;
import me.moonways.bridgenet.api.modern_x2_command.annotation.validate.AnnotationCommandValidateResult;
import me.moonways.bridgenet.api.modern_x2_command.obj.ExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.obj.entity.ConsoleCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.obj.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.event.CommandPostProcessEvent;
import me.moonways.bridgenet.api.modern_x2_command.event.CommandPreProcessEvent;
import me.moonways.bridgenet.api.modern_x2_command.obj.label.CommandLabelContext;
import me.moonways.bridgenet.api.modern_x2_command.obj.Command;
import me.moonways.bridgenet.api.modern_x2_command.obj.CommandInfo;
import me.moonways.bridgenet.api.modern_x2_command.process.CommandSearchStrategy;
import me.moonways.bridgenet.api.modern_x2_command.registration.CommandRegistrationService;
import me.moonways.bridgenet.api.modern_x2_command.process.result.CommandExecuteResult;

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
    private AnnotationCommandValidateManagement validateManagement;
    @Inject
    private ConsoleCommandSender consoleCommandSender;
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

    public synchronized boolean dispatch(EntityCommandSender sender, String label) {
        CommandLabelContext labelContext = CommandLabelContext.create(label);

        Optional<Command> searchedCommand = searchStrategy.search(labelContext);

        boolean found = searchedCommand.isPresent();

        if (found) {
            Command command = searchedCommand.get();
            postComposeDispatch(sender, command, labelContext);
        }

        return found;
    }

    private synchronized void postComposeDispatch(EntityCommandSender entity, Command command, CommandLabelContext labelContext) {
        ExecutionContext executionContext = ExecutionContext.create(entity, labelContext);

        if (validatePreDispatch(command, executionContext)) {
            return;
        }

        CommandExecuteResult postResult = getResultPostExecute(executionContext, command);
        invokeEvent(new CommandPostProcessEvent(executionContext, postResult));
    }

    private synchronized boolean validatePreDispatch(Command command, ExecutionContext executionContext) {
        return validatePreExecuteEvent(executionContext, command.getInfo()) && validatePreExecuteAnnotation(executionContext, command);
    }

    private boolean validatePreExecuteEvent(ExecutionContext executionContext, CommandInfo commandInfo) {
        CommandPreProcessEvent executeEvent = invokeEvent(new CommandPreProcessEvent(executionContext, commandInfo));
        return !executeEvent.isCancelled();
    }

    private boolean validatePreExecuteAnnotation(ExecutionContext executionContext, Command command) {
        AnnotationCommandValidateResult result = validateManagement.validate(executionContext, command);
        return result.isOk();
    }

    private CommandExecuteResult getResultPostExecute(ExecutionContext executionContext, Command command) {
        return invoke(executionContext, command);
    }

    @Async
    public void handleAsync(EntityCommandSender entity, String label) {
        this.dispatch(entity, label);
    }

    public void dispatchConsole(String label) {
        this.dispatch(consoleCommandSender, label);
    }

    public void unregisterAll() {
        registrationService.unregisterAll();
    }

    public void unregister(Class<?> cls) {
        registrationService.unregister(cls);
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> T invokeEvent(Event event) {
        return (T) eventService.fireEvent(event)
                .getFollower()
                .getCompleted();
    }

    private synchronized CommandExecuteResult invoke(ExecutionContext executionContext, Command command) {
        BeanMethod beanMethod = command.getBeanMethod();

        if (beanMethod.getRoot().getReturnType().isAssignableFrom(void.class)) {
            return CommandExecuteResult.empty();
        }

        return beanMethod.invoke(executionContext);
    }
}
