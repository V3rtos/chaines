package me.moonways.bridgenet.model.servers;

import me.moonways.bridgenet.rsi.service.RemoteService;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServersServiceModel extends RemoteService {

    /**
     * Получить список стандартных серверов для входа
     * игроков или других возможных операций.
     */
    List<EntityServer> getDefaultServers() throws RemoteException;

    /**
     * Получить список серверов, которые отвечают за редирект
     * при падении текущих серверов.
     */
    List<EntityServer> getFallbackServers() throws RemoteException;

    /**
     * Получить инициализированный и подключенный сервер
     * по его уникальному ключевому идентификатору.
     *
     * @param uuid - ключ сервера.
     */
    Optional<EntityServer> getServer(@NotNull UUID uuid) throws RemoteException;

    /**
     * Получить инициализированный и подключенный сервер
     * по его само-идентифицированному названию.
     *
     * @param serverName - название сервера.
     */
    Optional<EntityServer> getServer(@NotNull String serverName) throws RemoteException;

    /**
     * Проверить сервер, подключен ли он к системе по
     * его уникальному ключевому идентификатору.
     *
     * @param uuid - ключ сервера.
     */
    boolean hasServer(@NotNull UUID uuid) throws RemoteException;

    /**
     * Проверить сервер, подключен ли он к системе по
     * его само-идентифицированному названию.
     *
     * @param serverName - название сервера.
     */
    boolean hasServer(@NotNull String serverName) throws RemoteException;
}
