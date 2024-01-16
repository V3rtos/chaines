package me.moonways.bridgenet.api.modern_command;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {

    private final Map<String, CommandInfo> commands = new HashMap<>();

    public void add(@NotNull String name, @NotNull CommandInfo commandInfo) {
        commands.put(name.toLowerCase(), commandInfo);
    }

    public void remove(@NotNull String name) {
        commands.remove(name.toLowerCase());
    }

    public void removeAll() {
        commands.clear();
    }

    public CommandInfo get(@NotNull String name) {
        return commands.get(name.toLowerCase());
    }
}
