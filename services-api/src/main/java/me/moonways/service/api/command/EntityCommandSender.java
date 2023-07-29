package me.moonways.service.api.command;

import org.jetbrains.annotations.NotNull;

public interface EntityCommandSender {

    void sendMessage(@NotNull String message);

    boolean hasPermission(@NotNull String permission);
}
