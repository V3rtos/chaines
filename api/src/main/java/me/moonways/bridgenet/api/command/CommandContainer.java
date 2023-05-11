package me.moonways.bridgenet.api.command;

import me.moonways.bridgenet.api.command.exception.CommandAlreadyRegisteredException;
import me.moonways.bridgenet.api.command.exception.CommandNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {

    private final Map<String, Command> commandMap = new HashMap<>();

    public void addCommand(@NotNull String name, @NotNull Command command) {
        validateContains(name);

        commandMap.put(name.toLowerCase(), command);
    }

    public void removeCommand(@NotNull String name) {
        validateNull(name);

        commandMap.remove(name);
    }

    public void removeAll() {
        for (String commandName : commandMap.keySet()) {
            removeCommand(commandName);
        }
    }

    public Command getCommand(@NotNull String name) {
        validateNull(name);

        return commandMap.get(name.toLowerCase());
    }

    private void validateNull(@NotNull String name) {
        if (!commandMap.containsKey(name.toLowerCase())) {
            throw new CommandNotFoundException(String.format("Command %s not found", name));
        }
    }

    private void validateContains(@NotNull String name) {
        if (commandMap.containsKey(name.toLowerCase())) {
            throw new CommandAlreadyRegisteredException(String.format("Command %s already registered", name));
        }
    }
}
