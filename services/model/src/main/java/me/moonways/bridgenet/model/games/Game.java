package me.moonways.bridgenet.model.games;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface Game extends Remote {

    /**
     * Получить уникальный регистрационный номер игры.
     */
    UUID getUniqueId();

    /**
     * Получить название игры.
     */
    String getName();

    /**
     * Получить список загруженных карт в этой игре
     */
    List<String> getLoadedMaps() throws RemoteException;

    /**
     * Получить список загруженных серверов для этой игры
     */
    List<GameServer> getLoadedServers() throws RemoteException;

    /**
     * Получить список активных игровых карт для этой игры.
     */
    List<ActiveGame> getActiveGames() throws RemoteException;

    /**
     * Получить список загруженных серверов, запущенных
     * на указанной карте.
     *
     * @param map - название игровой карты.
     */
    List<GameServer> getLoadedServersByMap(@NotNull String map) throws RemoteException;

    /**
     * Получить список активных игровых карт, запущенных
     * на указанной карте.
     *
     * @param map - название игровой карты.
     */
    List<ActiveGame> getActiveGamesByMap(@NotNull String map) throws RemoteException;
}
