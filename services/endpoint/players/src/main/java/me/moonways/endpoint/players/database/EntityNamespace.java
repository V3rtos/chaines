package me.moonways.endpoint.players.database;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterAddon;
import me.moonways.bridgenet.jdbc.entity.persistence.Entity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityColumn;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityId;

import java.util.UUID;

@Getter
@Builder
@ToString
@Entity(name = "player_namespaces")
public class EntityNamespace {

    @Getter(onMethod_ = @EntityId(indexes = ParameterAddon.PRIMARY))
    private final transient long id;

    @Getter(onMethod_ = @EntityColumn(indexes = {ParameterAddon.PRIMARY, ParameterAddon.NOTNULL}))
    private final UUID uuid;

    @Getter(onMethod_ = @EntityColumn(order = 2))
    private final String name;
}
