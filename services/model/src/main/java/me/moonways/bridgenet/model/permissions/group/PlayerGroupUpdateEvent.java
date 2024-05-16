package me.moonways.bridgenet.model.permissions.group;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;

import java.util.UUID;

@Getter
@Builder
@ToString
public class PlayerGroupUpdateEvent implements Event {

    private final UUID playerId;
    private final PermissionGroup previousGroup;
    private final PermissionGroup newestGroup;
}
