package me.moonways.bridgenet.model.service.permissions.permission;

import me.moonways.bridgenet.model.event.PlayerPermissionAddEvent;
import me.moonways.bridgenet.model.event.PlayerPermissionRemoveEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PermissionsManager extends Remote {

    /**
     * Получить список активных прав доступа
     * пользователя на актуальный момент.
     *
     * @param playerName - имя пользователя.
     */
    Set<Permission> getActivePermissions(String playerName) throws RemoteException;

    /**
     * Получить список активных прав доступа
     * пользователя на актуальный момент.
     *
     * @param playerId - идентификатор пользователя.
     */
    Set<Permission> getActivePermissions(UUID playerId) throws RemoteException;

    /**
     * Добавить пользователю новое право доступа.
     *
     * @param playerName - имя пользователя.
     * @param permission - instance права доступа.
     * @return - прокинутое событие в сервисе событий
     * после успешной отработки текущей функции.
     */
    Optional<PlayerPermissionAddEvent> addPermission(String playerName, Permission permission) throws RemoteException;

    /**
     * Добавить пользователю новое право доступа.
     *
     * @param playerId   - идентификатор пользователя.
     * @param permission - instance права доступа.
     * @return - прокинутое событие в сервисе событий
     * после успешной отработки текущей функции.
     */
    Optional<PlayerPermissionAddEvent> addPermission(UUID playerId, Permission permission) throws RemoteException;

    /**
     * Добавить пользователю новое право доступа.
     *
     * @param playerName     - имя пользователя.
     * @param permissionName - наименование права доступа.
     * @return - прокинутое событие в сервисе событий
     * после успешной отработки текущей функции.
     */
    Optional<PlayerPermissionAddEvent> addPermission(String playerName, String permissionName) throws RemoteException;

    /**
     * Добавить пользователю новое право доступа.
     *
     * @param playerId       - идентификатор пользователя.
     * @param permissionName - наименование права доступа.
     * @return - прокинутое событие в сервисе событий
     * после успешной отработки текущей функции.
     */
    Optional<PlayerPermissionAddEvent> addPermission(UUID playerId, String permissionName) throws RemoteException;

    /**
     * Удалить у пользователя уже имеющееся у
     * него право доступа.
     *
     * @param playerName - имя пользователя.
     * @param permission - instance права доступа.
     * @return - прокинутое событие в сервисе событий
     * после успешной отработки текущей функции.
     */
    Optional<PlayerPermissionRemoveEvent> removePermission(String playerName, Permission permission) throws RemoteException;

    /**
     * Удалить у пользователя уже имеющееся у
     * него право доступа.
     *
     * @param playerId   - идентификатор пользователя.
     * @param permission - instance права доступа.
     * @return - прокинутое событие в сервисе событий
     * после успешной отработки текущей функции.
     */
    Optional<PlayerPermissionRemoveEvent> removePermission(UUID playerId, Permission permission) throws RemoteException;

    /**
     * Удалить у пользователя уже имеющееся у
     * него право доступа.
     *
     * @param playerName     - имя пользователя.
     * @param permissionName - наименование права доступа.
     * @return - прокинутое событие в сервисе событий
     * после успешной отработки текущей функции.
     */
    Optional<PlayerPermissionRemoveEvent> removePermission(String playerName, String permissionName) throws RemoteException;

    /**
     * Удалить у пользователя уже имеющееся у
     * него право доступа.
     *
     * @param playerId       - идентификатор пользователя.
     * @param permissionName - наименование права доступа.
     * @return - прокинутое событие в сервисе событий
     * после успешной отработки текущей функции.
     */
    Optional<PlayerPermissionRemoveEvent> removePermission(UUID playerId, String permissionName) throws RemoteException;

    /**
     * Очистить имеющийся список индивидуальных прав
     * доступа относительно пользователя.
     *
     * @param playerName - имя пользователя.
     */
    void clearPermissions(String playerName) throws RemoteException;

    /**
     * Очистить имеющийся список индивидуальных прав
     * доступа относительно пользователя.
     *
     * @param playerId - идентификатор пользователя.
     */
    void clearPermissions(UUID playerId) throws RemoteException;

    /**
     * Проверить наличие индивидуального права доступа
     * у пользователя в базе данных.
     *
     * @param playerName - имя пользователя
     * @param permission - instance проверяемого права доступа.
     */
    boolean hasPermission(String playerName, Permission permission) throws RemoteException;

    /**
     * Проверить наличие индивидуального права доступа
     * у пользователя в базе данных.
     *
     * @param playerId   - идентификатор пользователя
     * @param permission - instance проверяемого права доступа.
     */
    boolean hasPermission(UUID playerId, Permission permission) throws RemoteException;

    /**
     * Проверить наличие индивидуального права доступа
     * у пользователя в базе данных.
     *
     * @param playerName     - имя пользователя
     * @param permissionName - наименование проверяемого права доступа.
     */
    boolean hasPermission(String playerName, String permissionName) throws RemoteException;

    /**
     * Проверить наличие индивидуального права доступа
     * у пользователя в базе данных.
     *
     * @param playerId       - идентификатор пользователя
     * @param permissionName - наименование проверяемого права доступа.
     */
    boolean hasPermission(UUID playerId, String permissionName) throws RemoteException;
}
