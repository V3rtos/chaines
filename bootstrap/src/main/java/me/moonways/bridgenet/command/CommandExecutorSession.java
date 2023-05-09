package me.moonways.bridgenet.command;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.ConsoleSender;
import me.moonways.bridgenet.command.exception.CommandSenderCastException;
import me.moonways.bridgenet.command.sender.Sender;
import me.moonways.bridgenet.command.user.User;

@RequiredArgsConstructor
public class CommandExecutorSession {

    private final Sender sender;

    @SuppressWarnings("unchecked")
    public <T extends Sender> T of(Class<T> senderClass)  {
        if (senderClass.isAssignableFrom(User.class)) {
            return (T) sender;
        }

        if (senderClass.isAssignableFrom(ConsoleSender.class)) {
            return (T) sender;
        }

        throw new CommandSenderCastException(String.format("Can't cast sender to %s", senderClass.getName()));
    }
}
