package me.moonways.model.servers;

import me.moonways.bridgenet.rsi.service.RemoteService;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;

public interface ServersServiceModel extends RemoteService {

    void addServer(@NotNull EntityServer server) throws RemoteException;

    void removeServer(@NotNull EntityServer server) throws RemoteException;

    EntityServer getServer(@NotNull String serverName) throws RemoteException;

    boolean hasServer(@NotNull String serverName) throws RemoteException;
}
