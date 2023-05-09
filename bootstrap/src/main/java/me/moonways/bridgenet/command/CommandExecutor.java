package me.moonways.bridgenet.command;

import com.sun.istack.internal.NotNull;

import java.util.function.BiConsumer;

public interface CommandExecutor {

    BiConsumer<String[], CommandExecutorSession> getCommandConsumer();

    default void accept(@NotNull String[] args, @NotNull CommandExecutorSession sender) {
        getCommandConsumer().accept(args, sender);
    }
}
