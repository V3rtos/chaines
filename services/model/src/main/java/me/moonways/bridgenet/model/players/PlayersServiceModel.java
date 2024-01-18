package me.moonways.bridgenet.model.players;

import me.moonways.bridgenet.model.players.social.PlayerSocials;
import me.moonways.bridgenet.rsi.service.RemoteService;
import me.moonways.bridgenet.model.players.connection.PlayerConnection;
import me.moonways.bridgenet.model.players.leveling.PlayerLeveling;
import me.moonways.bridgenet.model.players.offline.PlayerOfflineManager;
import me.moonways.bridgenet.model.players.permission.PlayerPermissions;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.UUID;

public interface PlayersServiceModel extends RemoteService {

    PlayerConnection getPlayerConnection() throws RemoteException;

    PlayerLeveling getPlayerLeveling() throws RemoteException;

    PlayerPermissions getPlayerPermissions() throws RemoteException;

    PlayerSocials getPlayerSocials() throws RemoteException;

    PlayerOfflineManager getPlayerOfflineManager() throws RemoteException;

    String findPlayerName(@NotNull UUID playerUUID) throws RemoteException;

    UUID findPlayerId(@NotNull String playerName) throws RemoteException;
}
