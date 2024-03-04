package me.moonways.bridgenet.api.command.sender;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Autobind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
}
