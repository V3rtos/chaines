package me.moonways.bridgenet.api.command;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public interface CommandHandler {

    BiConsumer<String[], CommandSenderSession> getCommandHandler();

    default void accept(@NotNull String[] args, @NotNull CommandSenderSession sender) {
        getCommandHandler().accept(args, sender);
    }
}
