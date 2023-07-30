package me.moonways.bridgenet.api.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public final class CommandSession {

    private final EntityCommandSender sender;
    private final ArgumentArrayWrapper arguments;

    public <T extends EntityCommandSender> T cast(Class<T> objectCast) {
        return objectCast.cast(sender);
    }
}
