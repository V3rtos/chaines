package me.moonways.bridgenet.api.modern_command.entity;

import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ConsoleEntity implements EntityCommandSender {

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(@NotNull String message, @Nullable Object... replacements) {

    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return false;
    }

    @Override
    public EntityType getType() {
        return EntityType.CONSOLE;
    }

    @Override
    public boolean valueOf(EntityType entityType) {
        return entityType.equals(EntityType.CONSOLE) || entityType.equals(EntityType.BOTH);
    }

    @Override
    public UUID getUuid() {
        return UUID.fromString("6cf02237-59ef-430f-8110-83aab96eb14f");
    }
}
