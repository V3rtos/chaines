package me.moonways.bridgenet.command;

import com.sun.istack.internal.NotNull;
import me.moonways.bridgenet.command.exception.CommandAlreadyRegisteredException;
import me.moonways.bridgenet.command.exception.CommandNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {

    private final Map<String, Command> commandMap = new HashMap<>();

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

    public void addCommand(@NotNull String name, @NotNull Command command) {
        validateContains(name);

        commandMap.put(name.toLowerCase(), command);
    }

    public Command getCommand(@NotNull String name) {
        validateNull(name);

        return commandMap.get(name.toLowerCase());
    }

    public void unregisterAll() {
        for (String commandName : commandMap.keySet()) {
            removeCommand(commandName);
        }
    }

    public void removeCommand(@NotNull String name) {
        validateNull(name);

        commandMap.remove(name);
    }
}
