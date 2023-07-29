package me.moonways.service.api.command.console;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Component;
import me.moonways.service.api.command.EntityCommandSender;
import org.jetbrains.annotations.NotNull;

@Log4j2
@Component
public class ConsoleCommandSender implements EntityCommandSender {

    @Override
    public void sendMessage(String message) {
        log.info(message);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return true;
    }
}
