package me.moonways.bridgenet.client.api.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class GameDto {

    private String name;
    private String map;

    private int maxPlayers;

    private int playersInTeam;
}
