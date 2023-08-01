package me.moonways.bridgenet.api.command.option;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class OnlyConsoleSenderOption implements CommandOptionMatcher {

    @Override
    public boolean matches(@NotNull CommandSession session) {
        return !(session.getSender() instanceof ConsoleCommandSender);
    }

    @Override
    public void apply(@NotNull CommandSession session) {
        session.getSender().sendMessage("§4Only console sender!");
    }
}
