package me.moonways.endpoint.players;

import lombok.Getter;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.endpoint.players.connection.PlayerConnectionStub;
import me.moonways.endpoint.players.leveling.PlayerLevelingStub;
import me.moonways.endpoint.players.offline.PlayerOfflineManagerStub;
import me.moonways.endpoint.players.permission.PlayerPermissionsStub;
import me.moonways.endpoint.players.social.PlayerSocialsStub;
import me.moonways.model.players.PlayersServiceModel;
import me.moonways.model.players.connection.PlayerConnection;
import me.moonways.model.players.leveling.PlayerLeveling;
import me.moonways.model.players.offline.OfflineDao;
import me.moonways.model.players.offline.PlayerOfflineManager;
import me.moonways.model.players.permission.PlayerPermissions;
import me.moonways.model.players.social.PlayerSocials;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.UUID;

@Getter
public final class PlayersServiceEndpoint extends AbstractEndpointDefinition implements PlayersServiceModel {

    private static final long serialVersionUID = 5074638195342022234L;

    private final PlayerConnection playerConnection = new PlayerConnectionStub();
    private final PlayerLeveling playerLeveling = new PlayerLevelingStub();
    private final PlayerPermissions playerPermissions = new PlayerPermissionsStub();
    private final PlayerSocials playerSocials = new PlayerSocialsStub();
    private final PlayerOfflineManager playerOfflineManager = new PlayerOfflineManagerStub();

    @Inject
    private DependencyInjection dependencyInjection;

    public PlayersServiceEndpoint() throws RemoteException {
        super();
    }

    @Override
    public String findPlayerName(@NotNull UUID playerUUID) throws RemoteException {
        OfflineDao offlineDao = playerOfflineManager.readData(playerUUID);
        return offlineDao.getName();
    }

    @Override
    public UUID findPlayerId(@NotNull String playerName) throws RemoteException {
        OfflineDao offlineDao = playerOfflineManager.readData(playerName);
        return offlineDao.getUuid();
    }

    @PostConstruct
    void postInject() {
        dependencyInjection.imitateFakeBind(playerConnection);
        dependencyInjection.imitateFakeBind(playerLeveling);
        dependencyInjection.imitateFakeBind(playerPermissions);
        dependencyInjection.imitateFakeBind(playerSocials);
        dependencyInjection.imitateFakeBind(playerOfflineManager);
    }
}
