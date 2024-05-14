package me.moonways.bridgenet.test.engine.objects.jdbc;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterAddon;
import me.moonways.bridgenet.jdbc.entity.persistence.DatabaseEntity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityExternalParameter;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityId;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityParameter;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@DatabaseEntity(name = "users")
public class UserEntity {

    @Getter(onMethod_ = @EntityId)
    private final long id;

    @Getter(onMethod_ = @EntityParameter(id = "first_name", indexes = ParameterAddon.PRIMARY))
    private final String firstName;

    @Getter(onMethod_ = @EntityParameter(id = "last_name"))
    private final String lastName;

    @Getter(onMethod_ = @EntityParameter)
    private final int age;

    @Getter(onMethod_ = @EntityExternalParameter(id = "status_id"))
    private final StatusEntity statusEntity;
}
