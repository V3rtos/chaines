package me.moonways.bridgenet.model.games.exception;

import org.jetbrains.annotations.NotNull;

public class ArenaAlreadyRegisteredException extends RuntimeException {

    private static final long serialVersionUID = 165565350320270584L;

    public ArenaAlreadyRegisteredException(@NotNull String message) {
        super(message);
    }
}
