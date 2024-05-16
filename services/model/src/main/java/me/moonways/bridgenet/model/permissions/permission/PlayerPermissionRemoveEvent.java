package me.moonways.bridgenet.model.permissions.permission;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;

import java.util.UUID;

@Getter
@Builder
@ToString
public class PlayerPermissionRemoveEvent implements Event {

    private final UUID playerId;
    private final Permission permission;
}
