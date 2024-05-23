package me.moonways.bridgenet.model.service.games;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Game extends Remote {

    /**
     * Получить уникальный регистрационный номер игры.
     */
    UUID getUniqueId() throws RemoteException;

    /**
     * Получить название игры.
     */
    String getName() throws RemoteException;

    /**
     * Получить активную игровую карту, загруженную в игру
     * по ее уникальному идентификатору.
     *
     * @param uniqueId - ключ активной игры.
     */
    Optional<ActiveGame> getActiveGame(UUID uniqueId) throws RemoteException;

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
     * Получить список активных игровых карт, запущенных
     * на указанной карте.
     *
     * @param map - название игровой карты.
     */
    List<ActiveGame> getActiveGamesByMap(@NotNull String map) throws RemoteException;
}
