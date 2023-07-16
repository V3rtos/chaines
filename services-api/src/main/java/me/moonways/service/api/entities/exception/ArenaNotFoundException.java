package me.moonways.service.api.entities.exception;

import org.jetbrains.annotations.NotNull;

public class ArenaNotFoundException extends RuntimeException {

    public ArenaNotFoundException(@NotNull String message) {
        super(message);
    }
}
