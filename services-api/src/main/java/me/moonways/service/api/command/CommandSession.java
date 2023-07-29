package me.moonways.service.api.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.service.api.command.console.ConsoleCommandSender;
import me.moonways.service.api.command.exception.CommandSenderCastException;
import me.moonways.service.api.entities.player.EntityPlayer;

@Getter
@ToString
@RequiredArgsConstructor
public final class CommandSession {

    private final EntityCommandSender sender;
    private final ArgumentArrayWrapper argumentWrapper;

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
