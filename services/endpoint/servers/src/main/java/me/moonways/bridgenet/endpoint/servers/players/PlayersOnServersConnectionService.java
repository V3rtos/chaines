package me.moonways.bridgenet.endpoint.servers.players;

import lombok.Synchronized;
import me.moonways.bridgenet.api.inject.Autobind;

import java.util.*;
import java.util.stream.Collectors;

@Autobind
public class PlayersOnServersConnectionService {

    private final Map<UUID, UUID> playersOnServersMap = Collections.synchronizedMap(new HashMap<>());

    @Synchronized
    public void insert(UUID playerUUID, UUID serverUUID) {
        playersOnServersMap.put(playerUUID, serverUUID);
    }

    @Synchronized
    public void delete(UUID playerUUID) {
        playersOnServersMap.remove(playerUUID);
    }

    @Synchronized
    public Optional<UUID> getPlayerCurrentServerKey(UUID playerUUID) {
        return Optional.ofNullable(playersOnServersMap.get(playerUUID));
    }

    @Synchronized
    public List<UUID> getPlayersOnServerByKey(UUID serverKey) {
        return playersOnServersMap.keySet()
                .stream()
                .filter(uuid -> playersOnServersMap.get(uuid).equals(serverKey))
                .collect(Collectors.toList());
    }
}
