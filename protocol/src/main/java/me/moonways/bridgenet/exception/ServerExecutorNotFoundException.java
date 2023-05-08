package me.moonways.bridgenet.exception;

import org.jetbrains.annotations.NotNull;

public class ServerExecutorNotFoundException extends RuntimeException {

    public ServerExecutorNotFoundException(@NotNull String message) {
        super(message);
    }
}
