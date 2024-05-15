package me.moonways.bridgenet.test.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterAddon;
import me.moonways.bridgenet.jdbc.entity.persistence.DatabaseEntity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityId;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityParameter;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@DatabaseEntity(name = "statuses")
public class EntityStatus {

    @Getter(onMethod_ = @EntityId)
    private final long id;

    @Getter(onMethod_ = @EntityParameter(indexes = ParameterAddon.PRIMARY))
    private final String name;
}
