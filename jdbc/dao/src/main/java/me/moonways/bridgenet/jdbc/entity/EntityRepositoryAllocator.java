package me.moonways.bridgenet.jdbc.entity;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.compose.DatabaseComposer;

@RequiredArgsConstructor
public final class EntityRepositoryAllocator {

    private final DatabaseComposer composer;
    private final DatabaseConnection connection;

    public <T> EntityRepository<T> allocate(Class<T> entityClass) {
        return new ForceEntityRepository<>(entityClass, composer, connection);
    }
}
