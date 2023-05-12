package me.moonways.bridgenet.protocol.message.exception;

import org.jetbrains.annotations.NotNull;

public class MessageRegisterException extends RuntimeException {

    public MessageRegisterException(@NotNull String message) {
        super(message);
    }
}
