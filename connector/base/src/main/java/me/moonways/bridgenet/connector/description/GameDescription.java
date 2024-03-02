package me.moonways.bridgenet.connector.description;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class GameDescription {

    private String name;
    private String map;

    private int maxPlayers;

    private int playersInTeam;
}
