package me.moonways.bridgenet.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandProcessNotFoundException extends RuntimeException {

    public CommandProcessNotFoundException(@NotNull String command) {
        super(command);
    }
}
