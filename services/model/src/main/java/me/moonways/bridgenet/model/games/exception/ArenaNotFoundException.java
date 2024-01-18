package me.moonways.bridgenet.model.games.exception;

import org.jetbrains.annotations.NotNull;

public class ArenaNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3167534390713709294L;

    public ArenaNotFoundException(@NotNull String message) {
        super(message);
    }
}
