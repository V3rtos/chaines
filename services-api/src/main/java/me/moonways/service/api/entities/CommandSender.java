package me.moonways.service.api.entities;

import org.jetbrains.annotations.NotNull;

public interface CommandSender {

    String getName();

    boolean hasPermission(@NotNull String permission);

    void sendMessage(@NotNull String message);

    void performCommand(@NotNull String command);
}
