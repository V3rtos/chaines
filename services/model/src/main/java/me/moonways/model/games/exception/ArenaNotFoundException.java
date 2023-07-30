package me.moonways.model.games.exception;

import org.jetbrains.annotations.NotNull;

public class ArenaNotFoundException extends RuntimeException {

    public ArenaNotFoundException(@NotNull String message) {
        super(message);
    }
}
