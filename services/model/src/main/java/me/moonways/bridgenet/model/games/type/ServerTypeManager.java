package me.moonways.bridgenet.model.games.type;

import java.util.EnumMap;

public class ServerTypeManager {

    private final EnumMap<ServerType, ServerCriteria> serversTypesLinkageMap = new EnumMap<>(ServerType.class);
    private final EnumMap<ServerType, GameCriteria> gamesTypesLinkageMap = new EnumMap<>(ServerType.class);

    public ServerCriteria getServerCriteria(ServerType serverType) {
        return serversTypesLinkageMap.get(serverType);
    }

    public GameCriteria getGameCriteria(ServerType serverType) {
        return gamesTypesLinkageMap.get(serverType);
    }

    {
        serversTypesLinkageMap.put(ServerType.PROXY, ServerCriteria.MAINTAINER);
        serversTypesLinkageMap.put(ServerType.AUTH, ServerCriteria.MAINTAINER);
        serversTypesLinkageMap.put(ServerType.LIMBO, ServerCriteria.LOBBY);
        serversTypesLinkageMap.put(ServerType.FALLBACK_HUB, ServerCriteria.LOBBY);
        serversTypesLinkageMap.put(ServerType.SKYWARS, ServerCriteria.GAME);
        serversTypesLinkageMap.put(ServerType.BEDWARS, ServerCriteria.GAME);
        serversTypesLinkageMap.put(ServerType.BUILD_BATTLE, ServerCriteria.GAME);
        serversTypesLinkageMap.put(ServerType.MURDER_MYSTERY, ServerCriteria.GAME);
        serversTypesLinkageMap.put(ServerType.SKYBLOCK, ServerCriteria.ADVENTURE);
        serversTypesLinkageMap.put(ServerType.ONEBLOCK, ServerCriteria.ADVENTURE);
        serversTypesLinkageMap.put(ServerType.CLASSIC, ServerCriteria.ADVENTURE);
        serversTypesLinkageMap.put(ServerType.UNIVERSE, ServerCriteria.ADVENTURE);

        gamesTypesLinkageMap.put(ServerType.SKYWARS, GameCriteria.BATTLES);
        gamesTypesLinkageMap.put(ServerType.BEDWARS, GameCriteria.BATTLES);
        gamesTypesLinkageMap.put(ServerType.BUILD_BATTLE, GameCriteria.FANTASY);
        gamesTypesLinkageMap.put(ServerType.MURDER_MYSTERY, GameCriteria.SKILLS);
    }
}
