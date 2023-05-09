package me.moonways.bridgenet.command.sender;

import org.jetbrains.annotations.NotNull;

public interface Sender {

    String getName();

    boolean hasPermission(@NotNull String permission);

    void sendMessage(@NotNull String message);

    void performCommand(@NotNull String command);
}
