package me.moonways.model.players.connection;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface PlayerConnection extends Remote {

    void addConnectedPlayer(@NotNull ConnectedEntityPlayer connectedPlayer) throws RemoteException;

    void removeConnectedPlayer(@NotNull ConnectedEntityPlayer connectedPlayer) throws RemoteException;

    ConnectedEntityPlayer getConnectedPlayer(@NotNull UUID playerUUID) throws RemoteException;

    ConnectedEntityPlayer getConnectedPlayer(@NotNull String name) throws RemoteException;
}
