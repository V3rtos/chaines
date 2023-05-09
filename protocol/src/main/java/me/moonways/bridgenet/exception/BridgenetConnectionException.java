package me.moonways.bridgenet.exception;

import org.jetbrains.annotations.NotNull;

public class BridgenetConnectionException extends RuntimeException {

    public BridgenetConnectionException(@NotNull String message) {
        super(message);
    }

    public BridgenetConnectionException(@NotNull Throwable cause, @NotNull String message) {
        super(message, cause);
    }
}
