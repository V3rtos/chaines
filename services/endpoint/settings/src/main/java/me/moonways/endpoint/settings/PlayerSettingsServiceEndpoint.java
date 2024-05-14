package me.moonways.endpoint.settings;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.bridgenet.model.players.util.PlayerIdMap;
import me.moonways.bridgenet.model.settings.PlayerSettingsServiceModel;
import me.moonways.bridgenet.model.settings.Setting;
import me.moonways.bridgenet.model.settings.SettingID;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;

import java.rmi.RemoteException;
import java.util.*;

public class PlayerSettingsServiceEndpoint extends EndpointRemoteObject implements PlayerSettingsServiceModel {

    private static final long serialVersionUID = 4593442611825894452L;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final PlayerIdMap<PlayerSettings> playerSettingsCacheMap = new PlayerIdMap<>();

    @Inject
    private PlayersServiceModel playersServiceModel;
    @Inject
    private BeansService beansService;

    public PlayerSettingsServiceEndpoint() throws RemoteException {
        super();
    }

    private PlayerSettings createPlayerSettings(UUID playerId) {
        PlayerSettings playerSettings = new PlayerSettings(new HashMap<>(), playerId);

        beansService.inject(playerSettings);

        playerSettings.loadAll();
        return playerSettings;
    }

    @Override
    public Collection<SettingID<?>> getTotalSettings() throws RemoteException {
        return Arrays.asList(SettingID.TYPES);
    }

    @Override
    public <T> Setting<T> getSetting(UUID playerId, SettingID<T> id) throws RemoteException {
        PlayerSettings playerSettings = playerSettingsCacheMap.getOrPut(playerId, () -> createPlayerSettings(playerId));
        return playerSettings.get(id);
    }

    @Override
    public <T> Setting<T> getSetting(String playerName, SettingID<T> id) throws RemoteException {
        return getSetting(playersServiceModel.findPlayerId(playerName), id);
    }
}
