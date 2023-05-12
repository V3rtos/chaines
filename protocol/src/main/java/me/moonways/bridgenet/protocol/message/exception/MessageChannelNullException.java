package me.moonways.bridgenet.protocol.message.exception;

import org.jetbrains.annotations.NotNull;

public class MessageChannelNullException extends RuntimeException {

    public MessageChannelNullException(@NotNull String  message) {
        super(message);
    }
}
