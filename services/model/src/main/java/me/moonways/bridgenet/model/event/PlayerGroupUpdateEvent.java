package me.moonways.bridgenet.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.service.permissions.group.PermissionGroup;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PlayerGroupUpdateEvent implements Event {

    private final UUID playerId;
    private final PermissionGroup previousGroup;
    private final PermissionGroup newestGroup;
}
