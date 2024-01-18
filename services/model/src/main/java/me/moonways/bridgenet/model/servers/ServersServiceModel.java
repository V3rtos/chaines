package me.moonways.bridgenet.model.servers;

import me.moonways.bridgenet.rsi.service.RemoteService;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServersServiceModel extends RemoteService {

    List<EntityServer> getDefaultServers() throws RemoteException;

    List<EntityServer> getFallbackServers() throws RemoteException;

    Optional<EntityServer> getServer(@NotNull UUID uuid) throws RemoteException;

    Optional<EntityServer> getServer(@NotNull String serverName) throws RemoteException;

    boolean hasServer(@NotNull UUID uuid) throws RemoteException;

    boolean hasServer(@NotNull String serverName) throws RemoteException;
}
