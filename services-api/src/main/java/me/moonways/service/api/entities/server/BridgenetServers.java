package me.moonways.service.api.entities.server;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BridgenetServers extends Remote {

    void addServer(@NotNull EntityServer server) throws RemoteException;

    void removeServer(@NotNull EntityServer server) throws RemoteException;

    EntityServer getServer(@NotNull String serverName) throws RemoteException;

    boolean hasServer(@NotNull String serverName) throws RemoteException;
}
