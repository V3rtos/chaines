package me.moonways.bridgenet.jdbc.entity.descriptor;

import lombok.*;
import me.moonways.bridgenet.jdbc.entity.EntityID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class EntityDescriptor {

    private final EntityID id;
    private final EntityParametersDescriptor parameters;

    private final String containerName;

    private final Class<?> rootClass;
}
