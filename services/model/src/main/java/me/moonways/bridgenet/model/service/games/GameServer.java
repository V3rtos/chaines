package me.moonways.bridgenet.model.service.games;

import me.moonways.bridgenet.model.service.servers.ServerInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс описывает реализацию игрового сервера,
 * хранящий в себе информацию о проведенных актуальных игр
 * на нем на данный момент
 */
public interface GameServer extends Remote {

    /**
     * Получить информацию о сервере на текущий момент.
     */
    ServerInfo getServerInfo() throws RemoteException;

    /**
     * Получить наилучшую выбранную активную игру
     * для входа на сервер.
     */
    Optional<ActiveGame> getBetterGameForJoin() throws RemoteException;

    /**
     * Получить список доступных активных игр
     */
    List<ActiveGame> getActiveGames() throws RemoteException;
}
