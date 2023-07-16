package me.moonways.service.api.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandAlreadyRegisteredException extends RuntimeException {

    public CommandAlreadyRegisteredException(@NotNull String name) {
        super(name);
    }
}
