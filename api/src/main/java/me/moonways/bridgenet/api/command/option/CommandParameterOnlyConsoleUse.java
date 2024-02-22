package me.moonways.bridgenet.api.command.option;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.modern_x2_command.entity.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandParameterOnlyConsoleUse implements CommandParameterMatcher {

    @Override
    public boolean matches(@NotNull CommandSession session) {
        return !(session.getSender() instanceof ConsoleCommandSender);
    }

    @Override
    public void process(@NotNull CommandSession session) {
        session.getSender().sendMessage("§4Only console sender!");
    }
}
