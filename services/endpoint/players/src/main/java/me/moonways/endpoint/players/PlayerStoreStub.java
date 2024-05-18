package me.moonways.endpoint.players;

import me.moonways.bridgenet.model.players.OfflinePlayer;
import me.moonways.bridgenet.model.players.Player;
import me.moonways.bridgenet.model.players.service.PlayerStore;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;

public final class PlayerStoreStub implements PlayerStore {

    @Override
    public OfflinePlayer getOffline(UUID id) throws RemoteException {
        return null;
    }

    @Override
    public OfflinePlayer getOffline(String name) throws RemoteException {
        return null;
    }

    @Override
    public Optional<Player> get(UUID id) throws RemoteException {
        return Optional.empty();
    }

    @Override
    public Optional<Player> get(String name) throws RemoteException {
        return Optional.empty();
    }

    @Override
    public String nameById(UUID id) throws RemoteException {
        return "";
    }

    @Override
    public UUID idByName(String name) throws RemoteException {
        return null;
    }
}
