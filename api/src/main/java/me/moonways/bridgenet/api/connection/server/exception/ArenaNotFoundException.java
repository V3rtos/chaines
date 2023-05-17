package me.moonways.bridgenet.api.connection.server.exception;

import org.jetbrains.annotations.NotNull;

public class ArenaNotFoundException extends RuntimeException {

    public ArenaNotFoundException(@NotNull String message) {
        super(message);
    }
}
