package me.moonways.bridgenet.exception;

import org.jetbrains.annotations.NotNull;

public class ResponseMessageNotFoundException extends RuntimeException {

    public ResponseMessageNotFoundException(@NotNull String message) {
        super(message);
    }
}
