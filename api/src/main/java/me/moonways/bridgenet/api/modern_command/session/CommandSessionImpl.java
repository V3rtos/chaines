package me.moonways.bridgenet.api.modern_command.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_command.entity.CommandEntity;

@RequiredArgsConstructor
@Getter
public class CommandSessionImpl implements CommandSession {

    private final CommandEntity entity;

    @Override
    public <E extends CommandEntity> E from(Class<E> entityClass) {
        return entityClass.cast(entity);
    }

    @Override
    public void block(long millis) {

    }

    @Override
    public void block() {

    }
}
