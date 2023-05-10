package me.moonways.bridgenet.protocol.message.exception;

import org.jetbrains.annotations.NotNull;

public class MessageHandleException extends RuntimeException {

    public MessageHandleException(@NotNull String message) {
        super(message);
    }
}
