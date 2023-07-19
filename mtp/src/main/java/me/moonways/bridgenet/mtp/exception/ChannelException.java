package me.moonways.bridgenet.mtp.exception;

import org.jetbrains.annotations.NotNull;

public class ChannelException extends RuntimeException {

    public ChannelException(@NotNull String message) {
        super(message);
    }

    public ChannelException(@NotNull Throwable cause, @NotNull String message) {
        super(message, cause);
    }
}
