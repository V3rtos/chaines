package me.moonways.endpoint.players.database;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterAddon;
import me.moonways.bridgenet.jdbc.entity.persistence.Entity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityExternal;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityColumn;

@Getter
@Builder
@ToString
@Entity(name = "players")
public class EntityPlayer {

    @Getter(onMethod_ = @EntityExternal(
            order = 1,
            indexes = {ParameterAddon.PRIMARY, ParameterAddon.NOTNULL}))
    private final EntityNamespace namespace;

    @Getter(onMethod_ = @EntityColumn(
            order = 2,
            indexes = ParameterAddon.NOTNULL))
    private final PlayerDescription description;
}
