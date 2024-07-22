package me.moonways.bridgenet.jdbc.entity.descriptor;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
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
