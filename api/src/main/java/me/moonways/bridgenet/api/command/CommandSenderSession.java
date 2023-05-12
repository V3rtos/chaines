package me.moonways.bridgenet.api.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.command.sender.CommandSender;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import me.moonways.bridgenet.api.command.exception.CommandSenderCastException;
import me.moonways.bridgenet.api.connection.player.Player;

@RequiredArgsConstructor
@Getter
public class CommandSenderSession {

    private final CommandSender commandSender;
    private final String[] arguments;

    @SuppressWarnings("unchecked")
    public <T extends CommandSender> T senderCast(Class<T> senderClass)  {
        if (senderClass.isAssignableFrom(Player.class)) {
            return (T) commandSender;
        }

        if (senderClass.isAssignableFrom(ConsoleCommandSender.class)) {
            return (T) commandSender;
        }

        throw new CommandSenderCastException(String.format("Can't cast sender to %s", senderClass.getName()));
    }
}
