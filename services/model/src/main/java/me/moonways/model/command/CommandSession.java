package me.moonways.model.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.model.command.console.ConsoleCommandSender;
import me.moonways.model.command.exception.CommandSenderCastException;
import me.moonways.model.players.EntityPlayer;

@Getter
@ToString
@RequiredArgsConstructor
public final class CommandSession {

    private final EntityCommandSender sender;
    private final ArgumentArrayWrapper arguments;

    @SuppressWarnings("unchecked")
    public <T extends EntityCommandSender> T cast(Class<T> objectCast) {
        if (objectCast.isAssignableFrom(EntityPlayer.class)) {
            return (T) sender;
        }

        if (objectCast.isAssignableFrom(ConsoleCommandSender.class)) {
            return (T) sender;
        }

        throw new CommandSenderCastException(String.format("Can't cast sender to %s", objectCast.getName()));
    }
}
