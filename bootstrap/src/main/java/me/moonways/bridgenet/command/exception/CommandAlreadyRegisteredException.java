package me.moonways.bridgenet.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandAlreadyRegisteredException extends RuntimeException {

    public CommandAlreadyRegisteredException(@NotNull String name) {
        super(name);
    }
}
