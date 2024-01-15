package me.moonways.bridgenet.api.command.sender;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityCommandSender {

    void sendMessage(@Nullable String message);

    void sendMessage(@NotNull String message, @Nullable Object... replacements);

    boolean hasPermission(@NotNull String permission);
}
