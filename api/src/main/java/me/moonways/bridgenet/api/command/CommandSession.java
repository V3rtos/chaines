package me.moonways.bridgenet.api.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.command.wrapper.WrappedArguments;
import me.moonways.bridgenet.api.command.wrapper.WrappedCommand;

@ToString
@RequiredArgsConstructor
public final class CommandSession {

    private final WrappedCommand wrappedCommand;

    @Getter
    private final EntityCommandSender sender;

    @Getter
    private final WrappedArguments arguments;

    public <T extends EntityCommandSender> T getSender(Class<T> objectCast) {
        return objectCast.cast(sender);
    }
}
