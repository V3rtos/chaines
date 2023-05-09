package me.moonways.bridgenet.api.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandSenderCastException extends RuntimeException {

    public CommandSenderCastException(@NotNull String message) {
        super(message);
    }
}
