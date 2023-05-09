package me.moonways.bridgenet.api.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandProcessNotFoundException extends RuntimeException {

    public CommandProcessNotFoundException(@NotNull String command) {
        super(command);
    }
}
