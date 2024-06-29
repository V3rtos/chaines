package me.moonways.bridgenet.jdbc.entity;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.compose.DatabaseComposer;

@RequiredArgsConstructor
public final class EntityRepositoryFactory {

    private final DatabaseComposer composer;
    private final DatabaseConnection connection;

    public <T> EntityRepository<T> fromEntityType(Class<T> entityClass) {
        EntityOperationComposer.prepareTablesStore(connection);
        return new ForceEntityRepository<>(entityClass, composer, connection);
    }

    @SuppressWarnings("unchecked")
    public <T> EntityRepository<T> fromEntityType(T entity) {
        return (EntityRepository<T>) new ForceEntityRepository<>(entity.getClass(), composer, connection);
    }
}
