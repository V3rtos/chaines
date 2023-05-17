package me.moonways.bridgenet.api.connection.server.exception;

import org.jetbrains.annotations.NotNull;

public class ArenaAlreadyRegisteredException extends RuntimeException {

    public ArenaAlreadyRegisteredException(@NotNull String message) {
        super(message);
    }
}
