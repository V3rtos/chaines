package me.moonways.bridgenet.mtp.message.exception;

import org.jetbrains.annotations.NotNull;

public class MessageHandleException extends RuntimeException {

    public MessageHandleException(@NotNull String message) {
        super(message);
    }
}
