package me.moonways.bridgenet.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.service.permissions.permission.Permission;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PlayerPermissionAddEvent implements Event {

    private final UUID playerId;
    private final Permission permission;
}
