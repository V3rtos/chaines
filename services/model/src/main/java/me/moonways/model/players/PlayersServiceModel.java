package me.moonways.model.players;

import me.moonways.bridgenet.rsi.service.RemoteService;
import me.moonways.model.players.offline.OfflineEntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.UUID;

public interface PlayersServiceModel extends RemoteService {

    OfflineEntityPlayer getOfflinePlayer(@NotNull UUID playerUUID) throws RemoteException;

    OfflineEntityPlayer getOfflinePlayer(@NotNull String playerName) throws RemoteException;

    void addConnectedPlayer(@NotNull ConnectedEntityPlayer connectedPlayer) throws RemoteException;

    void removeConnectedPlayer(@NotNull ConnectedEntityPlayer connectedPlayer) throws RemoteException;

    String findPlayerName(@NotNull UUID playerUUID) throws RemoteException;

    UUID findPlayerId(@NotNull String playerName) throws RemoteException;

    ConnectedEntityPlayer getConnectedPlayer(@NotNull UUID playerUUID) throws RemoteException;

    ConnectedEntityPlayer getConnectedPlayer(@NotNull String name) throws RemoteException;
}
