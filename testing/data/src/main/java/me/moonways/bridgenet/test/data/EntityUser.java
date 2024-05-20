package me.moonways.bridgenet.test.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterSignature;
import me.moonways.bridgenet.jdbc.entity.persistence.Entity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityExternalParameter;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityId;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityParameter;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@Entity(name = "users")
public class EntityUser {

    @Getter(onMethod_ = @EntityId)
    private final long id;

    @Getter(onMethod_ = @EntityParameter(id = "first_name", indexes = ParameterSignature.PRIMARY))
    private final String firstName;

    @Getter(onMethod_ = @EntityParameter(id = "last_name"))
    private final String lastName;

    @Getter(onMethod_ = @EntityParameter)
    private final int age;

    @Getter(onMethod_ = @EntityExternalParameter(id = "status_id"))
    private final EntityStatus status;
}
