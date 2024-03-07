package me.moonways.bridgenet.api.command.api.process;

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
import me.moonways.bridgenet.api.command.InjectCommand;
import me.moonways.bridgenet.api.command.api.process.verification.inject.validate.CommandAnnotationValidateManagement;
import me.moonways.bridgenet.api.command.api.process.verification.inject.validate.CommandAnnotationValidateResult;
import me.moonways.bridgenet.api.command.api.uses.CommandExecutionContext;
import me.moonways.bridgenet.api.command.api.uses.entity.ConsoleCommandSender;
import me.moonways.bridgenet.api.command.api.uses.entity.EntityCommandSender;
import me.moonways.bridgenet.api.command.api.event.CommandPostProcessEvent;
import me.moonways.bridgenet.api.command.api.event.CommandPreProcessEvent;
import me.moonways.bridgenet.api.command.api.label.CommandLabelContext;
import me.moonways.bridgenet.api.command.api.uses.Command;
import me.moonways.bridgenet.api.command.api.uses.CommandInfo;
import me.moonways.bridgenet.api.command.api.label.search.CommandSearchStrategy;
import me.moonways.bridgenet.api.command.api.process.registration.CommandRegistrationService;
import me.moonways.bridgenet.api.command.api.process.execution.CommandExecuteResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис, который управляет командами для дальнейшей работы.
 * Данный объект позволяет вызвать команду синхронно/асинхронно, зарегистрировать,
 * снять регистрацию. Получить список всех команд для дальнейшей работы.
 */
@WaitTypeAnnotationProcessor(InjectCommand.class)
@Autobind
@EnableDecorators
public class CommandService {

    @Inject
    private EventService eventService;

    @Inject
    private CommandSearchStrategy searchStrategy;
    @Inject
    private CommandAnnotationValidateManagement validateManagement;
    @Inject
    private ConsoleCommandSender consoleCommandSender;
    @Inject
    private CommandRegistrationService registrationService;

    @GetTypeAnnotationProcessor
    private TypeAnnotationProcessorResult<Object> commandsResult;

    /**
     * Зарегистрировать все команды.
     */
    @PostConstruct
    private void registerAll() {
        List<Bean> beansList = commandsResult.toBeansList();
        beansList.forEach(bean -> registrationService.register(bean));
    }

    /**
     * Зарегистрировать команду по классу и его объекту.
     *
     * @param cls - класс команды.
     * @param parent - объект команды.
     */
    public void register(Class<?> cls, Object parent) {
        registrationService.register(cls, parent);
    }

    /**
     * Зарегистрировать команда по бину.
     *
     * @param bean - бин команды.
     */
    public void register(Bean bean) {
        registrationService.register(bean);
    }

    /**
     * Выполнить команду от типа любого отправителя.
     *
     * @param sender - отправитель.
     * @param label - строка.
     */
    public synchronized Optional<CommandExecutionContext> dispatch(EntityCommandSender sender, String label) {
        Optional<Command> searchedCommand = searchStrategy.search(label);

        if (searchedCommand.isPresent()) {
            Command command = searchedCommand.get();
            return executeCommand(sender, command,
                    CommandLabelContext.create(command, label));
        }

        return Optional.empty();
    }

    /**
     * Выполнить команду от типа любого отправителя.
     *
     * @param sender - отправитель.
     * @param command - команда для выполнения.
     * @param labelContext - контекст строки.
     */
    private Optional<CommandExecutionContext> executeCommand(EntityCommandSender sender, Command command, CommandLabelContext labelContext) {
        CommandExecutionContext commandExecutionContext = CommandExecutionContext.create(sender, labelContext);

        if (validatePreDispatch(command, commandExecutionContext)) {
            CommandExecuteResult postResult = getResultPostExecute(commandExecutionContext, command);
            invokeEvent(new CommandPostProcessEvent(commandExecutionContext, postResult));
        }

        return Optional.of(commandExecutionContext);
    }

    /**
     * Проверить можем ли выполнить команду проходя по
     * событиям и аннотациям.
     *
     * @param command - команда для выполнения.
     * @param commandExecutionContext - контекст выполнения команды.
     */
    private boolean validatePreDispatch(Command command, CommandExecutionContext commandExecutionContext) {
        return validatePreExecuteEvent(commandExecutionContext, command.getInfo()) && validatePreExecuteAnnotation(commandExecutionContext, command);
    }

    /**
     * Проверить доступность выполнения команды после
     * выполнения события.
     *
     * @param commandExecutionContext - контекст выполнения команды.
     * @param commandInfo - информация о команде.
     */
    private boolean validatePreExecuteEvent(CommandExecutionContext commandExecutionContext, CommandInfo commandInfo) {
        CommandPreProcessEvent executeEvent = invokeEvent(new CommandPreProcessEvent(commandExecutionContext, commandInfo));
        return !executeEvent.isCancelled();
    }

    /**
     * Проверить доступность выполнения команды после
     * парсинга аннотаций.
     *
     * @param commandExecutionContext - контекст выполнения команды.
     * @param command - команда.
     */
    private boolean validatePreExecuteAnnotation(CommandExecutionContext commandExecutionContext, Command command) {
        CommandAnnotationValidateResult result = validateManagement.validate(commandExecutionContext, command);
        return result.isOk();
    }

    /**
     * Получить результат после выполнения команда.
     *
     * @param commandExecutionContext - контекст выполнения команды.
     * @param command - команда.
     */
    private CommandExecuteResult getResultPostExecute(CommandExecutionContext commandExecutionContext, Command command) {
        return invoke(commandExecutionContext, command);
    }

    /**
     * Выполнить асинхронно команду.
     *
     * @param sender - отправитель.
     * @param label - строка.
     */
    @Async
    public Optional<CommandExecutionContext> dispatchAsync(EntityCommandSender sender, String label) {
        return dispatch(sender, label);
    }

    /**
     * Выполнить команду от имени консоли.
     *
     * @param label - строка.
     */
    public Optional<CommandExecutionContext> dispatchConsole(String label) {
        return dispatch(consoleCommandSender, label);
    }

    /**
     * Снять регистрацию со всех команд.
     */
    public void unregisterAll() {
        registrationService.unregisterAll();
    }

    /**
     * Снять регистрацию с команды.
     *
     * @param cls - класс команды.
     */
    public void unregister(Class<?> cls) {
        registrationService.unregister(cls);
    }

    /**
     * Получить список всех зарегистрированных команд.
     */
    public List<String> getRegisteredCommands() {
        return registrationService.findAll()
                .stream().map(command -> command.getInfo().getName()).collect(Collectors.toList());
    }

    /**
     * Вызвать событие.
     *
     * @param event - событие.
     * @param <T> - тип обьекта, который наследует @param event
     */
    @SuppressWarnings("unchecked")
    private <T extends Event> T invokeEvent(Event event) {
        return (T) eventService.fireEvent(event)
                .getFollower()
                .getCompleted();
    }

    /**
     * Вызвать метод команды.
     *
     * @param commandExecutionContext - конекст выполнения команды.
     * @param command - команда.
     */
    private synchronized CommandExecuteResult invoke(CommandExecutionContext commandExecutionContext, Command command) {
        BeanMethod beanMethod = command.getBeanMethod();

        Object returnedResult = beanMethod.invoke(commandExecutionContext);

        if (beanMethod.getRoot().getReturnType().equals(void.class)) {
            return CommandExecuteResult.empty();
        }

        return (CommandExecuteResult) returnedResult;
    }
}
