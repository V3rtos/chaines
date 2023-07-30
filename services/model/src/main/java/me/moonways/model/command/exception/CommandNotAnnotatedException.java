package me.moonways.model.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandNotAnnotatedException extends RuntimeException {

    private static final long serialVersionUID = -1688977463741206721L;

    public CommandNotAnnotatedException(@NotNull String message) {
        super(message);
    }

    public CommandNotAnnotatedException(@NotNull String message, @NotNull Exception exception) {
        super(message, exception);
    }
}
