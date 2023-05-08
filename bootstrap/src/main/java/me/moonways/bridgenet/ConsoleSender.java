package me.moonways.bridgenet;

import me.moonways.bridgenet.command.sender.Sender;
import org.jetbrains.annotations.NotNull;

public class ConsoleSender implements Sender {

    @Override
    public String getName() {
        return "BRIDGENET CONTROL";
    }

    @Override
    public void sendMessage(@NotNull String message) {
        System.out.println(message);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return false;
    }
}
