package me.moonways.bridgenet.jdbc.core.compose.template;

import me.moonways.bridgenet.jdbc.core.compose.StorageType;
import me.moonways.bridgenet.jdbc.core.compose.TemplatedQuery;

public interface EjectionTemplate extends TemplatedQuery {

    EjectionTemplate entity(StorageType storageType);

    EjectionTemplate name(String specifiedName);
}
