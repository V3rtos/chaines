package me.moonways.bridgenet.protocol.exception;

import org.jetbrains.annotations.NotNull;

public class MessageEncoderPacketIsNullException extends RuntimeException {

    public MessageEncoderPacketIsNullException(@NotNull String message) {
        super(message);
    }
}
