package me.moonways.bridgenet.model.service.games;

import me.moonways.bridgenet.model.service.servers.EntityServer;
import me.moonways.bridgenet.rmi.service.RemoteService;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface GamesServiceModel extends RemoteService {

    /**
     * Получить игру по ее уникальному регистрационному
     * идентификатору.
     *
     * @param uuid - идентификатор игры.
     */
    Game getGame(@NotNull UUID uuid) throws RemoteException;

    /**
     * Получить игру по ее названию.
     *
     * @param name - название игры.
     */
    Game getGame(@NotNull String name) throws RemoteException;

    /**
     * Получить список всех загруженных игр
     */
    List<Game> getLoadedGames() throws RemoteException;

    /**
     * Проверить, является ли сервер игровым.
     *
     * @param server - сервер.
     */
    boolean isGame(@NotNull EntityServer server) throws RemoteException;
}
