package me.moonways.bridgenet.model.permissions;

import me.moonways.bridgenet.model.permissions.group.GroupsManager;
import me.moonways.bridgenet.model.permissions.permission.PermissionsManager;
import me.moonways.bridgenet.rsi.service.RemoteService;

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
