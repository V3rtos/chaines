package me.moonways.bridgenet.exception;

import org.jetbrains.annotations.NotNull;

public class MessageDecoderEmptyPacketException extends RuntimeException {

    public MessageDecoderEmptyPacketException(@NotNull String message) {
        super(message);
    }
}
