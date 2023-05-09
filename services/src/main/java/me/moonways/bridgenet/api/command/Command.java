package me.moonways.bridgenet.api.command;

import lombok.Getter;
import lombok.Setter;
import me.moonways.bridgenet.api.command.exception.CommandAlreadyRegisteredException;
import me.moonways.bridgenet.api.command.exception.CommandProcessNotFoundException;
import me.moonways.bridgenet.api.command.condition.CommandCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class Command {

    protected abstract void register();

    @Setter
    private String commandName;

    @Getter
    private CommandExecutorWrapper defaultCommand;
    private CommandExecutorWrapper helpCommand;

    private final Map<String, CommandExecutorWrapper> subcommandExecutorMap = new HashMap<>();

    private void validateSubcommand(@NotNull String command) {
        if (!subcommandExecutorMap.containsKey(command.toLowerCase())) {
            throw new CommandProcessNotFoundException(String.format("Can't find subcommand process %s in %s", command, commandName));
        }
    }

    private void validateDefaultCommand() {
        if (defaultCommand == null) {
            throw new CommandProcessNotFoundException(String.format("Can't find command process in %s", commandName));
        }
    }

    private void validateHelpCommand() {
        if (helpCommand == null) {
            throw new CommandProcessNotFoundException(String.format("Can't find help command process in %s", commandName));
        }
    }

    private void validateContains(@NotNull String name) {
        if (subcommandExecutorMap.containsKey(name.toLowerCase())) {
            throw new CommandAlreadyRegisteredException(String.format("Command %s already registered", name));
        }
    }

    public void addSubcommand(
            @NotNull String commandName,
            @NotNull CommandCondition commandCondition,
            @NotNull BiConsumer<String[], CommandExecutorSession> consumerCommand) {

        validateContains(commandName);

        CommandExecutorWrapper commandCompleterProvider = createCommandExecutor(commandCondition, consumerCommand);
        subcommandExecutorMap.put(commandName.toLowerCase(), commandCompleterProvider);
    }

    public void setCommand(
            @NotNull CommandCondition commandCondition,
            @NotNull BiConsumer<String[], CommandExecutorSession> consumerCommand) {

        this.defaultCommand = createCommandExecutor(commandCondition, consumerCommand);
    }

    public void setHelpCommand(
            @NotNull CommandCondition commandCondition,
            @NotNull BiConsumer<String[], CommandExecutorSession> consumerCommand) {

        this.helpCommand = createCommandExecutor(commandCondition, consumerCommand);
    }

    private void executeSubcommand(@NotNull String name, @NotNull String[] arguments,  @NotNull CommandExecutorSession sender) {
        validateSubcommand(name);

        CommandExecutorWrapper commandAccepterWrapper = getSubcommand(name.toLowerCase());
        commandAccepterWrapper.accept(arguments, sender);
    }

    private void executeHelpCommand(@NotNull String[] arguments, @NotNull CommandExecutorSession sender) {
        validateHelpCommand();

        helpCommand.accept(arguments, sender);
    }

    private void executeDefaultCommand(@NotNull String[] arguments, @NotNull CommandExecutorSession sender) {
        validateDefaultCommand();

        defaultCommand.accept(arguments, sender);
    }

    public void executeCommand(@NotNull String[] arguments,  @NotNull CommandExecutorSession sender) {
        if (arguments.length <= 1) {
            executeHelpCommand(arguments, sender);
            return;
        }

        String subcommand = arguments[1];

        if (containsSubcommand(subcommand)) {
            String[] argsWithoutSubcommand = Arrays.copyOfRange(arguments, 2, arguments.length);

            executeSubcommand(subcommand, argsWithoutSubcommand, sender);
            return;
        }

        executeDefaultCommand(arguments, sender);
    }

    private CommandExecutorWrapper getSubcommand(@NotNull String name) {
        return subcommandExecutorMap.get(name.toLowerCase());
    }

    private boolean containsSubcommand(@NotNull String name) {
        return subcommandExecutorMap.containsKey(name.toLowerCase());
    }

    private CommandExecutorWrapper createCommandExecutor(
            @NotNull CommandCondition commandCondition,
            @NotNull BiConsumer<String[], CommandExecutorSession> consumerCommand) {

        return new CommandExecutorWrapper(commandCondition, consumerCommand);
    }
}
