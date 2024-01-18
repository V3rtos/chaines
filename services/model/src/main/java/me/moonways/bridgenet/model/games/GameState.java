package me.moonways.bridgenet.model.games;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameState extends Remote {

    /**
     * Получить статус игрового сервера.
     */
    GameStatus getStatus() throws RemoteException;

    /**
     * Получить название карты игрового сервера.
     */
    String getMap() throws RemoteException;

    /**
     * Сверить статус игрового сервера на необходимый.
     * @param gameStatus - игровой статус.
     */
    boolean checkStatus(@NotNull GameStatus gameStatus);

    /**
     * Получить максимальное возможное количество игроков,
     * которые могут одновременно играть на данном игровом сервере.
     */
    int getMaxPlayers() throws RemoteException;

    /**
     * Получить максимальное возможное количество игроков
     * в команде.
     */
    int getPlayersInTeam() throws RemoteException;

    /**
     * Получить количество игроков, играющих или находящихся
     * в ожидании сейчас на сервере.
     */
    int getPlayers() throws RemoteException;

    /**
     * Получить количество наблюдателей за игрой
     */
    int getSpectators() throws RemoteException;

    /**
     * Получить общее суммарное количество игроков на данном
     * игровом сервере.
     */
    default int getTotalPlayers() throws RemoteException {
        int players = getPlayers();
        int spectators = getSpectators();

        return players + spectators;
    }
}
