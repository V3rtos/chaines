package me.moonways.bridgenet.protocol.message.exception;

import org.jetbrains.annotations.NotNull;

public class MessageResponseException extends RuntimeException {

    public MessageResponseException(@NotNull String message) {
        super(message);
    }
}
