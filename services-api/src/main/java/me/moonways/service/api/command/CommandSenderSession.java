package me.moonways.service.api.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.service.api.command.exception.CommandSenderCastException;
import me.moonways.service.api.entities.CommandSender;
import me.moonways.service.api.command.sender.ConsoleCommandSender;
import me.moonways.service.api.entities.player.EntityPlayer;

@RequiredArgsConstructor
@Getter
public class CommandSenderSession {

    private final CommandSender commandSender;
    private final String[] arguments;

    @SuppressWarnings("unchecked")
    public <T extends CommandSender> T senderCast(Class<T> senderClass)  {
        if (senderClass.isAssignableFrom(EntityPlayer.class)) {
            return (T) commandSender;
        }

        if (senderClass.isAssignableFrom(ConsoleCommandSender.class)) {
            return (T) commandSender;
        }

        throw new CommandSenderCastException(String.format("Can't cast sender to %s", senderClass.getName()));
    }
}
