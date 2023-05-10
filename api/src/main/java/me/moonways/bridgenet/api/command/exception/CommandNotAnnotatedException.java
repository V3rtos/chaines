package me.moonways.bridgenet.api.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandNotAnnotatedException extends RuntimeException {

    public CommandNotAnnotatedException(@NotNull String message) {
        super(message);
    }
}
