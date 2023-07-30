package me.moonways.endpoint.players;

import lombok.Getter;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostFactoryMethod;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.model.players.ConnectedEntityPlayer;
import me.moonways.model.players.PlayersServiceModel;
import me.moonways.model.players.offline.OfflineEntityPlayer;
import me.moonways.model.players.offline.OfflinePlayerData;
import net.conveno.jdbc.ConvenoRouter;
import net.conveno.jdbc.response.ConvenoResponse;
import net.conveno.jdbc.response.ConvenoResponseLine;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayersServiceEndpoint extends AbstractEndpointDefinition implements PlayersServiceModel {

    private static final long serialVersionUID = 5074638195342022234L;

    private final Map<UUID, ConnectedEntityPlayer> connectedPlayerByIdMap = new HashMap<>();

    @Getter
    private OfflinePlayerRepository repository;

    @Inject
    private ConvenoRouter convenoRouter;

    public PlayersServiceEndpoint() throws RemoteException {
        super();
    }

    @PostFactoryMethod
    public void injectRepository() {
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

    @Override
    public OfflineEntityPlayer getOfflinePlayer(@NotNull UUID playerUUID) {
        OfflinePlayerData offlinePlayerData = getOfflinePlayerDataByUUID(playerUUID);
        return new OfflineEntityPlayer(offlinePlayerData.getUuid(), offlinePlayerData.getName());
    }

    @Override
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

    @Override
    public void addConnectedPlayer(@NotNull ConnectedEntityPlayer connectedPlayer) {
        validateNull(connectedPlayer);
        connectedPlayerByIdMap.put(connectedPlayer.getUniqueId(), connectedPlayer);
    }

    @Override
    public void removeConnectedPlayer(@NotNull ConnectedEntityPlayer connectedPlayer) {
        validateNull(connectedPlayer);
        connectedPlayerByIdMap.remove(connectedPlayer.getUniqueId());
    }

    @Override
    public String findPlayerName(@NotNull UUID playerUUID) {
        OfflinePlayerData offlinePlayerData = getOfflinePlayerDataByUUID(playerUUID);
        return offlinePlayerData.getName();
    }

    @Override
    public UUID findPlayerId(@NotNull String playerName) {
        OfflinePlayerData offlinePlayerData = getOfflinePlayerDataByName(playerName);
        return offlinePlayerData.getUuid();
    }

    @Override
    public ConnectedEntityPlayer getConnectedPlayer(@NotNull UUID playerUUID) {
        return connectedPlayerByIdMap.get(playerUUID);
    }

    @Override
    public ConnectedEntityPlayer getConnectedPlayer(@NotNull String name) {
        return connectedPlayerByIdMap.values()
                .stream()
                .filter(connectedPlayer -> connectedPlayer.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
