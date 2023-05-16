package me.moonways.bridgenet.velocity.exception;

import org.jetbrains.annotations.NotNull;

public class ServerConnectException extends RuntimeException {

    public ServerConnectException(@NotNull String message) {
        super(message);
    }
}
