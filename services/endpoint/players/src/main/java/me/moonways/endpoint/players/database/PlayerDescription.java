package me.moonways.endpoint.players.database;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@Builder
@ToString
public class PlayerDescription {

    private final EntityNamespace namespace;

    private final UUID lastLoggedProxyId;
    private final long lastLoggedTime;

    private final int totalExperience;
}
