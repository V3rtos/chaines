package me.moonways.bridgenet.exception;

import org.jetbrains.annotations.NotNull;

public class BridgenetConnectionClosedException extends RuntimeException {

    public BridgenetConnectionClosedException(@NotNull Exception exception, @NotNull String message) {
        super(message, exception);
    }

    public BridgenetConnectionClosedException(@NotNull String message) {
        super(message);
    }
}
