package me.moonways.bridgenet.model.service.servers;

import me.moonways.bridgenet.rmi.service.RemoteService;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServersServiceModel extends RemoteService {

    /**
     * Получить список всех зарегистрированных серверов.
     */
    List<EntityServer> getTotalServers() throws RemoteException;

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
     * Получить список серверов, которые отвечают за редирект
     * при падении текущих серверов.
     */
    Optional<EntityServer> getServerExact(@NotNull String serverName) throws RemoteException;

    /**
     * Получить инициализированный и подключенный сервер
     * по его уникальному ключевому идентификатору.
     *
     * @param uuid - ключ сервера.
     */
    Optional<EntityServer> getServerExact(@NotNull UUID uuid) throws RemoteException;

    /**
     * Получить инициализированный и подключенный сервер
     * по его ПРИМЕРНОМУ само-идентифицированному названию.
     *
     * @param serverName - примерное название сервера.
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
