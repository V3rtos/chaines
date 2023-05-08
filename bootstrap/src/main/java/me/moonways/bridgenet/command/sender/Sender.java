package me.moonways.bridgenet.command.sender;

import org.jetbrains.annotations.NotNull;

public interface Sender {

    String getName();

    void sendMessage(@NotNull String message);

    boolean hasPermission(@NotNull String permission);
}
