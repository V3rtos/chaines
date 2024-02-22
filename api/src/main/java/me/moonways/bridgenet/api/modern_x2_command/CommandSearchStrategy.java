package me.moonways.bridgenet.api.modern_x2_command;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_x2_command.install.registry.CommandRegistry;

import java.util.Optional;

public class CommandSearchStrategy {

    @Inject
    private CommandRegistry registry;

    public Optional<Command> search(String label) {
        String[] arr = label.split(" ");

        String commandName = null;

        for (String str : arr) {
            if (!contains(str)) break;

            commandName = str;
            break;
        }

        return Optional.of(registry.get(commandName));
    }

    private boolean contains(String name) {
        return registry.contains(name);
    }
}
