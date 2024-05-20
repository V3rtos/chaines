package me.moonways.endpoint.permissions.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterSignature;
import me.moonways.bridgenet.jdbc.entity.persistence.Entity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityParameter;
import me.moonways.bridgenet.model.permissions.TemporalState;
import me.moonways.bridgenet.model.permissions.permission.Permission;

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

    @Getter(onMethod_ = @EntityParameter(order = 1, id = "player_id",
            indexes = {ParameterSignature.KEY}))
    private UUID playerId;

    @Getter(onMethod_ = @EntityParameter(order = 2, id = "permission",
            indexes = {ParameterSignature.KEY}))
    private String permission;

    @Getter(onMethod_ = @EntityParameter(order = 3, id = "expired_in"))
    private Long expirationTimeMillis;

    public boolean isExpired() {
        return expirationTimeMillis > 0 && System.currentTimeMillis() >= expirationTimeMillis;
    }
}
