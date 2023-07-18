package me.moonways.bridgenet.protocol.pipeline.exception;

import org.jetbrains.annotations.NotNull;

public class ChannelHandlerException extends RuntimeException {

    private static final long serialVersionUID = -6764387138018072816L;

    public ChannelHandlerException(@NotNull Throwable throwable, @NotNull String  message) {
        super(message, throwable);
    }
}
