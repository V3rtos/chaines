package me.moonways.service.api.entities.exception;

import org.jetbrains.annotations.NotNull;

public class ArenaAlreadyRegisteredException extends RuntimeException {

    public ArenaAlreadyRegisteredException(@NotNull String message) {
        super(message);
    }
}
