package me.moonways.service.api.games.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.service.api.games.type.old.ArcadeModeType;
import me.moonways.service.api.games.type.old.GameType;
import me.moonways.service.api.games.type.old.ServerModeType;
import me.moonways.service.api.games.type.old.ServerType;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Arena {

    public static Arena create(int maxPlayers, UUID arenaUUID, String mapName) {
        return new Arena(GameType.ARCADES, ArcadeModeType.SPEED_BUILDERS, maxPlayers, arenaUUID, mapName);
    }

    private final ServerType game;
    private final ServerModeType mode;

    private final int maxPlayers;

    private final UUID arenaUUID;

    private final String mapName;
}
