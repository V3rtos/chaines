package me.moonways.bridgenet.api.command.condition;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.command.Command;
import me.moonways.bridgenet.api.command.exception.CommandAccessDeniedException;
import me.moonways.bridgenet.api.command.exception.ConditionNotFoundException;
import me.moonways.bridgenet.api.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class CommandCondition {

    private final Command command;

    private final Map<CommandAccessDeniedType, Predicate<CommandSender>> predicates = new HashMap<>();

    public void addCondition(CommandAccessDeniedType accessDeniedMessageType, Predicate<CommandSender> sender) {
        predicates.put(accessDeniedMessageType, sender);
    }

    public void validateConditions(@NotNull CommandSender commandSender) {
        for (CommandAccessDeniedType accessDeniedType : predicates.keySet()) {
            validateCondition(commandSender, accessDeniedType);
        }
    }

    private void validateCondition(@NotNull CommandSender commandSender, @NotNull CommandAccessDeniedType commandAccessDeniedType) {
        Predicate<CommandSender> predicate = getPredicate(commandAccessDeniedType);

        if (!validCondition(commandSender, predicate)) throwAccessDeniedException(commandAccessDeniedType);
    }

    private void validateNull(@NotNull CommandAccessDeniedType accessDeniedType) {
        if (!predicates.containsKey(accessDeniedType))
            throw new ConditionNotFoundException(String.format("Can't find condition %s for class %s",
                    accessDeniedType.name(), command.getClass().getName()));
    }

    private void throwAccessDeniedException(@NotNull CommandAccessDeniedType blocker) {
        throw new CommandAccessDeniedException(blocker.getErrorMessage());
    }

    private boolean validCondition(@NotNull CommandSender commandSender, @NotNull Predicate<CommandSender> predicate) {
        return predicate.test(commandSender);
    }

    private Predicate<CommandSender> getPredicate(@NotNull CommandAccessDeniedType commandAccessDeniedType) {
        validateNull(commandAccessDeniedType);

        return predicates.get(commandAccessDeniedType);
    }
}
