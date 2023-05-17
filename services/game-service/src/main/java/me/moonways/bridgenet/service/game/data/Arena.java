package me.moonways.bridgenet.service.game.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Arena {

    public static Arena create(int maxPlayers, Game game, Mode mode, UUID arenaUUID, String mapName) {
        return new Arena(game, mode, maxPlayers, arenaUUID, mapName);
    }

    private final Game game;
    private final Mode mode;

    private final int maxPlayers;

    private final UUID arenaUUID;

    private final String mapName;
}
