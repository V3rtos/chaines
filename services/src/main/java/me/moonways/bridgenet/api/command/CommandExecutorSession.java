package me.moonways.bridgenet.api.command;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.command.sender.ConsoleSender;
import me.moonways.bridgenet.api.command.exception.CommandSenderCastException;
import me.moonways.bridgenet.api.command.sender.Sender;
import me.moonways.bridgenet.api.connection.player.Player;

@RequiredArgsConstructor
public class CommandExecutorSession {

    private final Sender sender;

    @SuppressWarnings("unchecked")
    public <T extends Sender> T senderCast(Class<T> senderClass)  {
        if (senderClass.isAssignableFrom(Player.class)) {
            return (T) sender;
        }

        if (senderClass.isAssignableFrom(ConsoleSender.class)) {
            return (T) sender;
        }

        throw new CommandSenderCastException(String.format("Can't cast sender to %s", senderClass.getName()));
    }
}
