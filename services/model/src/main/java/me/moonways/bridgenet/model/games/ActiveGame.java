package me.moonways.bridgenet.model.games;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface ActiveGame extends Remote {

    /**
     * Получить уникальный регистрационный номер активной игры.
     */
    UUID getUniqueId() throws RemoteException;

    /**
     * Получить текущую загруженную карту на данный сервер.
     */
    String getMap() throws RemoteException;

    /**
     * Получить текущее состояние игры
     */
    GameState getState() throws RemoteException;
}
