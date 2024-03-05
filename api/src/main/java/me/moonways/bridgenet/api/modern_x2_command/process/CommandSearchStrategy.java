package me.moonways.bridgenet.api.modern_x2_command.process;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_x2_command.objects.Command;
import me.moonways.bridgenet.api.modern_x2_command.objects.label.CommandLabelContext;
import me.moonways.bridgenet.api.modern_x2_command.registration.CommandRegistry;

import java.util.*;
import java.util.stream.Collectors;

@Autobind
public final class CommandSearchStrategy {

    @Inject
    private CommandRegistry registry;

    public Optional<Command> search(CommandLabelContext labelContext) {
        ListIterator<String> iterator = labelContext.getArguments()
                .stream()
                .collect(Collectors.toList())
                .listIterator();

        StringBuilder combinedWords = new StringBuilder();

        while (iterator.hasNext()) {
            String nextWord = iterator.next();

            combinedWords.append(nextWord).append(iterator.hasNext() ? " " : "");

            if (!contains(nextWord)) {
                String completedCombinedWords = combinedWords.toString();

                if (contains(completedCombinedWords)) {
                    return Optional.of(get(completedCombinedWords));
                }
            } else {
                return Optional.of(get(nextWord));
            }
        }
        return Optional.ofNullable(get(labelContext.getCommandName()));
    }

    private boolean contains(String name) {
        return registry.contains(UUID.nameUUIDFromBytes(name.getBytes()));
    }

    private Command get(String commandName) {
        return registry.get(UUID.nameUUIDFromBytes(commandName.getBytes()));
    }
}
