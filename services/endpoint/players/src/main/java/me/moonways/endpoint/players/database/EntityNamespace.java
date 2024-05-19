package me.moonways.endpoint.players.database;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterAddon;
import me.moonways.bridgenet.jdbc.entity.persistence.Entity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityId;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityParameter;

import java.util.UUID;

@Getter
@Builder
@ToString
@Entity(name = "player_namespaces")
public class EntityNamespace {

    @Getter(onMethod_ = @EntityId(indexes = ParameterAddon.PRIMARY))
    private final long id;

    @Getter(onMethod_ = @EntityParameter(indexes = {ParameterAddon.PRIMARY, ParameterAddon.NOTNULL}))
    private final UUID uuid;

    @Getter(onMethod_ = @EntityParameter(order = 2))
    private final String name;
}
