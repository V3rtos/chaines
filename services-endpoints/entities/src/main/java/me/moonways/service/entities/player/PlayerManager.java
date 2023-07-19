package me.moonways.service.entities.player;

import me.moonways.service.api.entities.player.BridgenetPlayers;
import me.moonways.service.api.entities.player.ConnectedEntityPlayer;
import lombok.Getter;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.service.api.entities.player.offline.OfflineEntityPlayer;
import me.moonways.service.api.entities.player.offline.OfflinePlayerData;
import net.conveno.jdbc.ConvenoRouter;
import net.conveno.jdbc.response.ConvenoResponse;
import net.conveno.jdbc.response.ConvenoResponseLine;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerManager extends UnicastRemoteObject implements BridgenetPlayers {

    private static final long serialVersionUID = 5074638195342022234L;

    private static final int PLAYER_ID_LABEL_INDEX = 1;
    private static final int PLAYER_NAME_LABEL_INDEX = 2;

    private final Map<UUID, ConnectedEntityPlayer> connectedPlayerByIdMap = new HashMap<>();

    @Getter
    private OfflinePlayerRepository repository;

    public PlayerManager() throws RemoteException {
        super();
    }

    public void injectRepository(ConvenoRouter convenoRouter) {
        repository = convenoRouter.getRepository(OfflinePlayerRepository.class);
        repository.validateTableExists();
    }

    private OfflinePlayerData getOfflinePlayerDataByUUID(UUID playerUUID) {
        ConvenoResponse offlinePlayerResponse = repository.getByUUID(playerUUID);
        ConvenoResponseLine first = offlinePlayerResponse.first();

        if (first == null) {
            throw new NullPointerException("player response by id");
        }

        return OfflinePlayerData.readResponse(first);
    }

    private OfflinePlayerData getOfflinePlayerDataByName(String playerName) {
        ConvenoResponse offlinePlayerResponse = repository.getByName(playerName);
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
