package me.moonways.endpoint.players;

import lombok.Getter;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.endpoint.players.connection.PlayerConnectionStub;
import me.moonways.endpoint.players.leveling.PlayerLevelingStub;
import me.moonways.endpoint.players.offline.OfflinePlayerRepository;
import me.moonways.model.players.PlayersServiceModel;
import me.moonways.model.players.connection.PlayerConnection;
import me.moonways.model.players.leveling.PlayerLeveling;
import me.moonways.model.players.offline.OfflineEntityPlayer;
import me.moonways.model.players.offline.OfflinePlayerData;
import net.conveno.jdbc.ConvenoRouter;
import net.conveno.jdbc.response.ConvenoResponse;
import net.conveno.jdbc.response.ConvenoResponseLine;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.UUID;

public final class PlayersServiceEndpoint extends AbstractEndpointDefinition implements PlayersServiceModel {

    private static final long serialVersionUID = 5074638195342022234L;

    private OfflinePlayerRepository repository;

    @Getter
    private final PlayerConnection playerConnection = new PlayerConnectionStub();

    @Getter
    private final PlayerLeveling playerLeveling = new PlayerLevelingStub();

    public PlayersServiceEndpoint() throws RemoteException {
        super();
    }

    @PostConstruct
    public void injectRepository() {
        DependencyInjection dependencyInjection = getDependencyInjection();
        ConvenoRouter convenoRouter = (ConvenoRouter) dependencyInjection.getContainer().findInstance(ConvenoRouter.class);

        repository = convenoRouter.getRepository(OfflinePlayerRepository.class);
        repository.executeTableValid();
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
}
