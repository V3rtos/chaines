package me.moonways.bridgenet.api.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandNotIdentifiedException extends RuntimeException {

    public CommandNotIdentifiedException(@NotNull String message) {
        super(message);
    }
}
