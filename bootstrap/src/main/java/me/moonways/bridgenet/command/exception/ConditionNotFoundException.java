package me.moonways.bridgenet.command.exception;

import org.jetbrains.annotations.NotNull;

public class ConditionNotFoundException extends RuntimeException {

    public ConditionNotFoundException(@NotNull String message) {
        super(message);
    }
}
