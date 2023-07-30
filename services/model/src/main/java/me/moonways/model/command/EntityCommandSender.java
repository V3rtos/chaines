package me.moonways.model.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityCommandSender {

    void sendMessage(@Nullable String message);

    boolean hasPermission(@NotNull String permission);
}
