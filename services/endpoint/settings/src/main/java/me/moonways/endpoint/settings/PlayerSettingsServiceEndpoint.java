package me.moonways.endpoint.settings;

import me.moonways.bridgenet.model.settings.PlayerSettingsServiceModel;
import me.moonways.bridgenet.model.settings.Setting;
import me.moonways.bridgenet.model.settings.SettingID;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

public class PlayerSettingsServiceEndpoint extends EndpointRemoteObject implements PlayerSettingsServiceModel {
    private static final long serialVersionUID = 4593442611825894452L;

    public PlayerSettingsServiceEndpoint() throws RemoteException {
        super();
    }

    @Override
    public Collection<SettingID<?>> getTotalSettings() throws RemoteException {
        return null;
    }

    @Override
    public <T> Setting<T> getSetting(UUID playerId, SettingID<T> id) throws RemoteException {
        return null;
    }

    @Override
    public <T> Setting<T> getSetting(String playerName, SettingID<T> id) throws RemoteException {
        return null;
    }
}
