package me.moonways.model.players;

import me.moonways.bridgenet.rsi.service.RemoteService;
import me.moonways.model.players.connection.PlayerConnection;
import me.moonways.model.players.leveling.PlayerLeveling;
import me.moonways.model.players.offline.OfflineEntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.UUID;

public interface PlayersServiceModel extends RemoteService {

    PlayerConnection getPlayerConnection() throws RemoteException;

    PlayerLeveling getPlayerLeveling() throws RemoteException;

    OfflineEntityPlayer getOfflinePlayer(@NotNull UUID playerUUID) throws RemoteException;

    OfflineEntityPlayer getOfflinePlayer(@NotNull String playerName) throws RemoteException;

    String findPlayerName(@NotNull UUID playerUUID) throws RemoteException;

    UUID findPlayerId(@NotNull String playerName) throws RemoteException;
}
