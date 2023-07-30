package me.moonways.model.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -1089968034913141143L;

    public CommandNotFoundException(@NotNull String message) {
        super(message);
    }

    public CommandNotFoundException(@NotNull String message, @NotNull Exception exception) {
        super(message, exception);
    }
}
