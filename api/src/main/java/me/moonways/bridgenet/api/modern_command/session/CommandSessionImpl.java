package me.moonways.bridgenet.api.modern_command.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class CommandSessionImpl implements CommandSession {

    private final EntityCommandSender entity;
    private final UUID uuid;

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public <E extends EntityCommandSender> E from(Class<E> entityClass) {
        return null;
    }

    @Override
    public void block(long millis) {

    }

    @Override
    public void block() {

    }
}
