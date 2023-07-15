package me.moonways.bridgenet.api.command.sender;

import me.moonways.bridgenet.injection.Component;
import me.moonways.services.api.entities.CommandSender;
import org.jetbrains.annotations.NotNull;

@Component
public class ConsoleCommandSender implements CommandSender {

    private static final String CONSOLE_SENDER_NAME = "BRIDGENET_CONTROL";

    @Override
    public String getName() {
        return CONSOLE_SENDER_NAME;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return true;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        System.out.println(message);
    }

    @Override
    public void performCommand(@NotNull String command) {
    }
}
