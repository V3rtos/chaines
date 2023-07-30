package me.moonways.bridgenet.api.command.sender;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Component;
import org.jetbrains.annotations.NotNull;

@Log4j2
@Component
public final class ConsoleCommandSender implements EntityCommandSender {

    @Override
    public void sendMessage(String message) {
        log.info(message);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return false;
    }
}
