package me.moonways.bridgenet.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandAccessDeniedException extends RuntimeException {

    public CommandAccessDeniedException(@NotNull String message) {
        super(message);
    }
}
