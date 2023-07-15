package me.moonways.service.entity.player;

import java.util.UUID;
import lombok.Getter;
import me.moonways.bridgenet.injection.Component;
import me.moonways.bridgenet.injection.PostFactoryMethod;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.services.api.entities.player.BridgenetPlayers;
import me.moonways.services.api.entities.player.ConnectedEntityPlayer;
import me.moonways.services.api.entities.player.offline.OfflineEntityPlayer;
import me.moonways.services.api.entities.player.offline.OfflinePlayerData;
import net.conveno.jdbc.ConvenoRouter;
import net.conveno.jdbc.response.ConvenoResponse;
import net.conveno.jdbc.response.ConvenoResponseLine;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public final class PlayerManager implements BridgenetPlayers {

    private static final int PLAYER_ID_LABEL_INDEX = 1;
    private static final int PLAYER_NAME_LABEL_INDEX = 2;

    private final Map<UUID, ConnectedEntityPlayer> connectedPlayerByIdMap = Collections.synchronizedMap(new HashMap<>());

    @Getter
    private OfflinePlayerRepository offlinePlayerRepository;

    @Inject
    private ConvenoRouter convenoRouter;

    @PostFactoryMethod
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

    public OfflineEntityPlayer getOfflinePlayer(@NotNull UUID playerUUID) {
        OfflinePlayerData offlinePlayerData = getOfflinePlayerDataByUUID(playerUUID);
        return new OfflineEntityPlayer(offlinePlayerData.getUuid(), offlinePlayerData.getName());
    }

    public OfflineEntityPlayer getOfflinePlayer(@NotNull String playerName) {
        OfflinePlayerData offlinePlayerData = getOfflinePlayerDataByName(playerName);
        return new OfflineEntityPlayer(offlinePlayerData.getUuid(), offlinePlayerData.getName());
    }

    private void validateNull(ConnectedEntityPlayer connectedPlayer) {
        if (connectedPlayer == null) {
            throw new NullPointerException("connected player");
        }
    }

    private void validateNull(String playerName) {
        if (playerName == null) {
            throw new NullPointerException("connected player name");
        }
    }

    public void addConnectedPlayer(@NotNull ConnectedEntityPlayer connectedPlayer) {
        validateNull(connectedPlayer);
        connectedPlayerByIdMap.put(connectedPlayer.getUniqueId(), connectedPlayer);
    }

    public void removeConnectedPlayer(@NotNull ConnectedEntityPlayer connectedPlayer) {
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

    public ConnectedEntityPlayer getConnectedPlayer(@NotNull UUID playerUUID) {
        return connectedPlayerByIdMap.get(playerUUID);
    }

    public ConnectedEntityPlayer getConnectedPlayer(@NotNull String name) {
        return connectedPlayerByIdMap.values()
                .stream()
                .filter(connectedPlayer -> connectedPlayer.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
