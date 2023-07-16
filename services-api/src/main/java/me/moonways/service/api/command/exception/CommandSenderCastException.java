package me.moonways.service.api.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandSenderCastException extends RuntimeException {

    public CommandSenderCastException(@NotNull String message) {
        super(message);
    }
}
