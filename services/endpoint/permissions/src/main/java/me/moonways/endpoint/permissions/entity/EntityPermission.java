package me.moonways.endpoint.permissions.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterAddon;
import me.moonways.bridgenet.jdbc.entity.persistence.Entity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityColumn;
import me.moonways.bridgenet.model.service.permissions.TemporalState;
import me.moonways.bridgenet.model.service.permissions.permission.Permission;

import java.util.UUID;

@Builder
@ToString
@Entity(name = "player_permissions")
public class EntityPermission {

    public static EntityPermission fromPermission(UUID playerId, Permission permission) {
        TemporalState temporalState = permission.getTemporalState();
        return EntityPermission.builder()
                .playerId(playerId)
                .permission(permission.getName())
                .expirationTimeMillis(temporalState.withActualTimeMillis())
                .build();
    }

    @Getter(onMethod_ = @EntityColumn(order = 1, id = "player_id",
            indexes = {ParameterAddon.KEY}))
    private UUID playerId;

    @Getter(onMethod_ = @EntityColumn(order = 2, id = "permission",
            indexes = {ParameterAddon.KEY}))
    private String permission;

    @Getter(onMethod_ = @EntityColumn(order = 3, id = "expired_in"))
    private Long expirationTimeMillis;

    public boolean isExpired() {
        return expirationTimeMillis > 0 && System.currentTimeMillis() >= expirationTimeMillis;
    }
}
