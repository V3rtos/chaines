package me.moonways.bridgenet.api.modern_x2_command;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_x2_command.label.CommandLabelContext;
import me.moonways.bridgenet.api.modern_x2_command.registration.CommandRegistry;

import java.util.*;
import java.util.stream.Collectors;

@Autobind
public class CommandSearchStrategy {

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
                    return Optional.of(registry.get(completedCombinedWords));
                }
            } else {
                return Optional.of(registry.get(nextWord));
            }
        }
        return Optional.ofNullable(registry.get(labelContext.getCommandName()));
    }

    private boolean contains(String name) {
        return registry.contains(name);
    }
}
