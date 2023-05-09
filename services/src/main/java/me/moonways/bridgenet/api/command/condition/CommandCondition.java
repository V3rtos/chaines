package me.moonways.bridgenet.api.command.condition;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.command.Command;
import me.moonways.bridgenet.api.command.exception.CommandAccessDeniedException;
import me.moonways.bridgenet.api.command.exception.ConditionNotFoundException;
import me.moonways.bridgenet.api.command.sender.Sender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class CommandCondition {

    private final Command command;
    private final Map<CommandAccessDeniedType, Predicate<Sender>> predicates = new HashMap<>();

    public void addCondition(CommandAccessDeniedType accessDeniedMessageType, Predicate<Sender> sender) {
        predicates.put(accessDeniedMessageType, sender);
    }

    public void validateConditions(@NotNull Sender sender) {
        for (CommandAccessDeniedType accessDeniedType : predicates.keySet()) {
            validateCondition(sender, accessDeniedType);
        }
    }

    private void validateCondition(@NotNull Sender sender, @NotNull CommandAccessDeniedType commandAccessDeniedType) {
        Predicate<Sender> predicate = getPredicate(commandAccessDeniedType);

        if (!validCondition(sender, predicate)) throwAccessDeniedException(commandAccessDeniedType);
    }

    private void validateNull(@NotNull CommandAccessDeniedType accessDeniedType) {
        if (!predicates.containsKey(accessDeniedType))
            throw new ConditionNotFoundException(String.format("Can't find condition %s for class %s",
                    accessDeniedType.name(), command.getClass().getName()));
    }

    private void throwAccessDeniedException(@NotNull CommandAccessDeniedType blocker) {
        throw new CommandAccessDeniedException(blocker.getErrorMessage());
    }

    private boolean validCondition(@NotNull Sender sender, @NotNull Predicate<Sender> predicate) {
        return predicate.test(sender);
    }

    private Predicate<Sender> getPredicate(@NotNull CommandAccessDeniedType commandAccessDeniedType) {
        validateNull(commandAccessDeniedType);

        return predicates.get(commandAccessDeniedType);
    }
}
