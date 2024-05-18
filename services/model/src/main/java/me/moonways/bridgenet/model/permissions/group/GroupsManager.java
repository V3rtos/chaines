package me.moonways.bridgenet.model.permissions.group;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс представляет собой удаленный интерфейс
 * для управления группами прав игроков.
 * Этот интерфейс позволяет получать информацию о различных
 * типах групп, а также устанавливать и проверять группы
 * для конкретных игроков.
 */
public interface GroupsManager extends Remote {

    /**
     * @return Список всех групп разрешений.
     */
    List<PermissionGroup> getTotalGroups() throws RemoteException;

    /**
     * @return Список групп, связанных с пожертвованиями.
     */
    List<PermissionGroup> getDonateGroups() throws RemoteException;

    /**
     * @return Список временных групп разрешений.
     */
    List<PermissionGroup> getTemporalGroups() throws RemoteException;

    /**
     * @return Список коммерческих групп разрешений.
     */
    List<PermissionGroup> getCommercialGroups() throws RemoteException;

    /**
     * @return Список персональных групп разрешений.
     */
    List<PermissionGroup> getPersonalGroups() throws RemoteException;

    /**
     * @return Список технических персональных групп разрешений.
     */
    List<PermissionGroup> getTechPersonalGroups() throws RemoteException;

    /**
     * @return Список групп владельцев.
     */
    List<PermissionGroup> getOwnerGroups() throws RemoteException;

    /**
     * Получить группу по идентификатору.
     *
     * @param groupId - идентификатор группы.
     *
     * @return - содержащий группу, если она найдена, иначе пусто.
     */
    Optional<PermissionGroup> getGroup(int groupId) throws RemoteException;

    /**
     * Получить группу по имени.
     *
     * @param groupName - имя группы.
     *
     * @return - содержащий группу, если она найдена, иначе пусто.
     */
    Optional<PermissionGroup> getGroup(String groupName) throws RemoteException;

    /**
     * Получить группу игрока по его имени.
     *
     * @param playerName - имя игрока.
     *
     * @return - содержащий группу игрока, если она найдена, иначе пусто.
     */
    Optional<PermissionGroup> getPlayerGroup(String playerName) throws RemoteException;

    /**
     * Получить группу игрока по его идентификатору.
     *
     * @param playerId - идентификатор игрока.
     *
     * @return - содержащий группу игрока, если она найдена, иначе пусто.
     */
    Optional<PermissionGroup> getPlayerGroup(UUID playerId) throws RemoteException;

    /**
     * Установить группу для игрока по его имени.
     *
     * @param playerName - имя игрока.
     * @param group - выдаваемая группа.
     *
     * @return - содержащий объект события обновления группы игрока, если установка прошла успешно, иначе пусто.
     */
    Optional<PlayerGroupUpdateEvent> setPlayerGroup(String playerName, PermissionGroup group) throws RemoteException;

    /**
     * Установить группу для игрока по его идентификатору.
     *
     * @param playerId - идентификатор игрока.
     * @param group - выдаваемая группа.
     *
     * @return - содержащий объект события обновления группы игрока, если установка прошла успешно, иначе пусто.
     */
    Optional<PlayerGroupUpdateEvent> setPlayerGroup(UUID playerId, PermissionGroup group) throws RemoteException;

    /**
     * Получить группу по умолчанию.
     */
    PermissionGroup getDefault() throws RemoteException;

    /**
     * Проверить, является ли игрок группой по умолчанию.
     * @param playerName - имя игрока.
     */
    boolean isDefault(String playerName) throws RemoteException;

    /**
     * Проверить, является ли игрок группой по умолчанию.
     * @param playerId - идентификатор игрока.
     */
    boolean isDefault(UUID playerId) throws RemoteException;

    /**
     * Проверить, имеет ли игрок пожертвования.
     * @param playerName - имя игрока.
     */
    boolean isDonated(String playerName) throws RemoteException;

    /**
     * Проверить, имеет ли игрок пожертвования.
     * @param playerId - идентификатор игрока.
     */
    boolean isDonated(UUID playerId) throws RemoteException;

    /**
     * Проверить, является ли игрок персональной группой.
     * @param playerName - имя игрока.
     */
    boolean isPersonal(String playerName) throws RemoteException;

    /**
     * Проверить, является ли игрок персональной группой.
     * @param playerId - идентификатор игрока.
     */
    boolean isPersonal(UUID playerId) throws RemoteException;

    /**
     * Проверить, является ли игрок технической персональной группой.
     * @param playerName - имя игрока.
     */
    boolean isTechPersonal(String playerName) throws RemoteException;

    /**
     * Проверить, является ли игрок технической персональной группой.
     * @param playerId - идентификатор игрока.
     */
    boolean isTechPersonal(UUID playerId) throws RemoteException;

    /**
     * Проверить, является ли игрок группой владельцев.
     * @param playerName - имя игрока.
     */
    boolean isOwner(String playerName) throws RemoteException;

    /**
     * Проверить, является ли игрок группой владельцев.
     * @param playerId - идентификатор игрока.
     */
    boolean isOwner(UUID playerId) throws RemoteException;
}
