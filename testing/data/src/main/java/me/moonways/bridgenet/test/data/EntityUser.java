package me.moonways.bridgenet.test.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterAddon;
import me.moonways.bridgenet.jdbc.entity.persistence.Entity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityExternal;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityId;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityColumn;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@Entity(name = "users")
public class EntityUser {

    @Getter(onMethod_ = @EntityId)
    private final long id;

    @Getter(onMethod_ = @EntityColumn(id = "first_name", indexes = ParameterAddon.PRIMARY))
    private final String firstName;

    @Getter(onMethod_ = @EntityColumn(id = "last_name"))
    private final String lastName;

    @Getter(onMethod_ = @EntityColumn)
    private final int age;

    @Getter(onMethod_ = @EntityExternal(id = "status_id"))
    private final EntityStatus status;
}
