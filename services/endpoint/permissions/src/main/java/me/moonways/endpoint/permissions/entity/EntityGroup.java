package me.moonways.endpoint.permissions.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterSignature;
import me.moonways.bridgenet.jdbc.entity.persistence.Entity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityParameter;
import me.moonways.bridgenet.model.permissions.TemporalState;
import me.moonways.bridgenet.model.permissions.group.PermissionGroup;

import java.util.UUID;

@Builder
@ToString
@Entity(name = "player_groups")
public class EntityGroup {

    public static EntityGroup fromGroup(UUID playerId, PermissionGroup group) {
        TemporalState temporalState = group.getTemporalState();
        return EntityGroup.builder()
                .playerId(playerId)
                .groupId(group.getId())
                .expirationTimeMillis(temporalState.withActualTimeMillis())
                .build();
    }

    @Getter(onMethod_ = @EntityParameter(order = 1, id = "player_id",
            indexes = {ParameterSignature.PRIMARY}))
    private UUID playerId;

    @Getter(onMethod_ = @EntityParameter(order = 2, id = "group_id",
            indexes = {ParameterSignature.PRIMARY}))
    private Integer groupId;

    @Getter(onMethod_ = @EntityParameter(order = 3, id = "expired_in"))
    private Long expirationTimeMillis;

    public boolean isExpired() {
        return expirationTimeMillis > 0 && System.currentTimeMillis() >= expirationTimeMillis;
    }
}
