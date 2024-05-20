package me.moonways.bridgenet.jdbc.entity.modern;

import me.moonways.bridgenet.api.inject.Autobind;

@Autobind
public final class DatabaseEntityManager {

    public <T> Descriptor<T> descriptor(Class<T> entityType) {
        return new Descriptor<>();
    }

    public <T> Condition<T> condition(Class<T> entityType) {
        return new Condition<>();
    }
}
