package me.moonways.bridgenet.protocol.pipeline.exception;

import org.jetbrains.annotations.NotNull;

public class ChannelHandlerException extends RuntimeException {

    public ChannelHandlerException(@NotNull Throwable throwable, @NotNull String  message) {
        super(message, throwable);
    }
}
