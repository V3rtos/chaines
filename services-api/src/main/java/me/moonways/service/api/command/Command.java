package me.moonways.service.api.command;

import lombok.Getter;
import lombok.Setter;
import me.moonways.service.api.command.exception.CommandAlreadyRegisteredException;
import me.moonways.service.api.command.exception.CommandProcessNotFoundException;
import me.moonways.service.api.command.condition.CommandCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class Command {

    protected abstract void register();

    @Setter
    private String commandName;

    @Getter
    private CommandHandler defaultCommand;
    private CommandHandler helpCommand;

    private final Map<String, CommandHandlerWrapper> subcommandExecutorMap = new HashMap<>();

    private void executeSubcommand(@NotNull String name, @NotNull String[] arguments,  @NotNull CommandSenderSession sender) {
        validateSubcommand(name);

        CommandHandlerWrapper commandHandlerWrapper = getSubcommand(name.toLowerCase());
        commandHandlerWrapper.accept(arguments, sender);
    }

    private void executeHelpCommand(@NotNull String[] arguments, @NotNull CommandSenderSession sender) {
        validateCommand(helpCommand);

        helpCommand.accept(arguments, sender);
    }

    private void executeDefaultCommand(@NotNull String[] arguments, @NotNull CommandSenderSession sender) {
        validateCommand(defaultCommand);

        defaultCommand.accept(arguments, sender);
    }

    // TODO: 11.05.2023 Переписать 
    public void executeCommand(@NotNull String[] arguments,  @NotNull CommandSenderSession sender) {
        if (arguments.length <= 1) {
            executeHelpCommand(arguments, sender);
            return;
        }

        String subcommand = arguments[1]; // TODO: 11.05.2023  

        if (containsSubcommand(subcommand)) { 
            String[] argsWithoutSubcommand = Arrays.copyOfRange(arguments, 2, arguments.length); // TODO: 11.05.2023  

            executeSubcommand(subcommand, argsWithoutSubcommand, sender);
            return;
        }

        executeDefaultCommand(arguments, sender);
    }

    public void addSubcommand(
            @NotNull String commandName,
            @NotNull CommandCondition commandCondition,
            @NotNull BiConsumer<String[], CommandSenderSession> consumerCommand) {

        validateContains(commandName);

        CommandHandlerWrapper commandCompleterProvider = createCommandHandler(commandCondition, consumerCommand);
        subcommandExecutorMap.put(commandName.toLowerCase(), commandCompleterProvider);
    }

    public void setCommand(
            @NotNull CommandCondition commandCondition,
            @NotNull BiConsumer<String[], CommandSenderSession> consumerCommand) {

        this.defaultCommand = createCommandHandler(commandCondition, consumerCommand);
    }

    public void setHelpCommand(
            @NotNull CommandCondition commandCondition,
            @NotNull BiConsumer<String[], CommandSenderSession> consumerCommand) {

        this.helpCommand = createCommandHandler(commandCondition, consumerCommand);
    }
    
    private CommandHandlerWrapper getSubcommand(@NotNull String name) {
        return subcommandExecutorMap.get(name.toLowerCase());
    }

    private boolean containsSubcommand(@NotNull String name) {
        return subcommandExecutorMap.containsKey(name.toLowerCase());
    }

    private CommandHandlerWrapper createCommandHandler(
            @NotNull CommandCondition commandCondition,
            @NotNull BiConsumer<String[], CommandSenderSession> consumerCommand) {

        return new CommandHandlerWrapper(commandCondition, consumerCommand);
    }

    private void validateSubcommand(@NotNull String command) {
        if (!subcommandExecutorMap.containsKey(command.toLowerCase())) {
            throw new CommandProcessNotFoundException(String.format("Can't find subcommand process %s in %s", command, commandName));
        }
    }

    private void validateCommand(@Nullable CommandHandler commandHandler) {
        if (commandHandler == null) {
            throw new CommandProcessNotFoundException(String.format("Can't find command process in %s", commandName));
        }
    }

    private void validateContains(@NotNull String name) {
        if (subcommandExecutorMap.containsKey(name.toLowerCase())) {
            throw new CommandAlreadyRegisteredException(String.format("Command %s already registered", name));
        }
    }
}
