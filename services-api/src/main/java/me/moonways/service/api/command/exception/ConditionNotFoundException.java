package me.moonways.service.api.command.exception;

import org.jetbrains.annotations.NotNull;

public class ConditionNotFoundException extends RuntimeException {

    public ConditionNotFoundException(@NotNull String message) {
        super(message);
    }
}
