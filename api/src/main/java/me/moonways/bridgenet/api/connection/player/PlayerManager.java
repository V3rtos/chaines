package me.moonways.bridgenet.api.connection.player;

import java.util.UUID;
import lombok.Getter;
import me.moonways.bridgenet.service.inject.Component;
import me.moonways.bridgenet.service.inject.InitMethod;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.bridgenet.services.connection.player.OfflinePlayerData;
import me.moonways.bridgenet.services.connection.player.OfflinePlayerRepository;
import net.conveno.jdbc.ConvenoRouter;
import net.conveno.jdbc.response.ConvenoResponse;
import net.conveno.jdbc.response.ConvenoResponseLine;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public final class PlayerManager {

    private static final int PLAYER_ID_LABEL_INDEX = 1;
    private static final int PLAYER_NAME_LABEL_INDEX = 2;

    private final Map<UUID, ConnectedPlayer> connectedPlayerByIdMap = Collections.synchronizedMap(new HashMap<>());

    @Getter
    private OfflinePlayerRepository offlinePlayerRepository;

    @Inject
    private ConvenoRouter convenoRouter;

    @InitMethod
    private void initRepository() {
        offlinePlayerRepository = convenoRouter.getRepository(OfflinePlayerRepository.class);
        offlinePlayerRepository.validateTableExists();
    }

    private OfflinePlayerData getOfflinePlayerDataByUUID(UUID playerUUID) {
        ConvenoResponse offlinePlayerResponse = offlinePlayerRepository.getByUUID(playerUUID);
        ConvenoResponseLine first = offlinePlayerResponse.first();

        if (first == null) {
            throw new NullPointerException("player response by id");
        }

        return OfflinePlayerData.readResponse(first);
    }

    private OfflinePlayerData getOfflinePlayerDataByName(String playerName) {
        ConvenoResponse offlinePlayerResponse = offlinePlayerRepository.getByName(playerName);
        ConvenoResponseLine first = offlinePlayerResponse.first();

        if (first == null) {
            throw new NullPointerException("player response by name");
        }

        return OfflinePlayerData.readResponse(first);
    }

    public OfflinePlayer getOfflinePlayer(@NotNull UUID playerUUID) {
        OfflinePlayerData offlinePlayerData = getOfflinePlayerDataByUUID(playerUUID);
        return new OfflinePlayer(offlinePlayerData.getUuid(), offlinePlayerData.getName());
    }

    public OfflinePlayer getOfflinePlayer(@NotNull String playerName) {
        OfflinePlayerData offlinePlayerData = getOfflinePlayerDataByName(playerName);
        return new OfflinePlayer(offlinePlayerData.getUuid(), offlinePlayerData.getName());
    }

    private void validateNull(ConnectedPlayer connectedPlayer) {
        if (connectedPlayer == null) {
            throw new NullPointerException("connected player");
        }
    }

    private void validateNull(String playerName) {
        if (playerName == null) {
            throw new NullPointerException("connected player name");
        }
    }

    public void addConnectedPlayer(@NotNull ConnectedPlayer connectedPlayer) {
        validateNull(connectedPlayer);
        connectedPlayerByIdMap.put(connectedPlayer.getUniqueId(), connectedPlayer);
    }

    public void removeConnectedPlayer(@NotNull ConnectedPlayer connectedPlayer) {
        validateNull(connectedPlayer);
        connectedPlayerByIdMap.remove(connectedPlayer.getUniqueId());
    }

    public String findPlayerName(@NotNull UUID playerUUID) {
        OfflinePlayerData offlinePlayerData = getOfflinePlayerDataByUUID(playerUUID);
        return offlinePlayerData.getName();
    }

    public UUID findPlayerId(@NotNull String playerName) {
        OfflinePlayerData offlinePlayerData = getOfflinePlayerDataByName(playerName);
        return offlinePlayerData.getUuid();
    }

    public ConnectedPlayer getConnectedPlayer(@NotNull UUID playerUUID) {
        return connectedPlayerByIdMap.get(playerUUID);
    }

    public ConnectedPlayer getConnectedPlayer(@NotNull String name) {
        return connectedPlayerByIdMap.values()
                .stream()
                .filter(connectedPlayer -> connectedPlayer.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
