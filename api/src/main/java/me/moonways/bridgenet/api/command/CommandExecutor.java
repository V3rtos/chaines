package me.moonways.bridgenet.api.command;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public interface CommandExecutor {

    BiConsumer<String[], CommandExecutorSession> getCommandConsumer();

    default void accept(@NotNull String[] args, @NotNull CommandExecutorSession sender) {
        getCommandConsumer().accept(args, sender);
    }
}
