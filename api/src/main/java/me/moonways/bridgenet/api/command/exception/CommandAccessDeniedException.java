package me.moonways.bridgenet.api.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandAccessDeniedException extends RuntimeException {

    public CommandAccessDeniedException(@NotNull String message) {
        super(message);
    }
}
