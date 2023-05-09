package me.moonways.bridgenet.protocol.exception;

import org.jetbrains.annotations.NotNull;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(@NotNull String message) {
        super(message);
    }
}
