package me.moonways.bridgenet.api.modern_x2_command.objects.entity;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Autobind;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Log4j2
@Autobind
public final class ConsoleCommandSender implements EntityCommandSender {

    public static ConsoleCommandSender INSTANE = new ConsoleCommandSender();

    @Override
    public void sendMessage(String message) {
        log.info(message);
    }

    @Override
    public void sendMessage(String message, @Nullable Object... replacements) {
        sendMessage(String.format(message, replacements));
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public EntitySenderType getType() {
        return EntitySenderType.CONSOLE;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isInstanceOf(EntitySenderType entityType) {
        return entityType.equals(EntitySenderType.CONSOLE);
    }

    @Override
    public UUID getUuid() {
        return UUID.fromString("6cf02237-59ef-430f-8110-83aab96eb14f");
    }
}
