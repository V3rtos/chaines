package me.moonways.bridgenet.api.modern_command.entity;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.inject.Autobind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Log4j2
@Autobind
public final class ConsoleCommandSender implements EntityCommandSender {

    @Override
    public void sendMessage(String message) {
        log.info(message);
    }

    @Override
    public void sendMessage(@NotNull String message, @Nullable Object... replacements) {
        sendMessage(String.format(message, replacements));
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return true;
    }

    @Override
    public EntityType getType() {
        return EntityType.CONSOLE;
    }

    @Override
    public boolean isInstanceOf(EntityType entityType) {
        return entityType.equals(EntityType.CONSOLE);
    }

    @Override
    public UUID getUuid() {
        return UUID.fromString("6cf02237-59ef-430f-8110-83aab96eb14f");
    }
}
