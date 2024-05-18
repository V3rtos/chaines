package me.moonways.endpoint.players.database;

import lombok.*;

@Getter
@EqualsAndHashCode
@Builder
@ToString
public class PlayerDescription {

    private final int totalExperience;
    private final long lastLoggedTime;
}
