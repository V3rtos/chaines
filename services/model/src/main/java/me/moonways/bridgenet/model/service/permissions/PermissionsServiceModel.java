package me.moonways.bridgenet.model.service.permissions;

import me.moonways.bridgenet.model.service.permissions.group.GroupsManager;
import me.moonways.bridgenet.model.service.permissions.permission.PermissionsManager;
import me.moonways.bridgenet.rmi.service.RemoteService;

import java.rmi.RemoteException;

public interface PermissionsServiceModel extends RemoteService {

    /**
     * Получить менеджер управления группами прав
     * относительно пользователей, отдельного репозитория базы
     * данных и списка всевозможных зарегистрированных групп.
     */
    GroupsManager getGroups() throws RemoteException;

    /**
     * Получить менеджер управления индивидуальными
     * правами доступа относительно пользователей и
     * отдельного репозитория базы данных.
     */
    PermissionsManager getPermissions() throws RemoteException;
}
