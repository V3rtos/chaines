package me.moonways.bridgenet.model.permissions;

import me.moonways.bridgenet.model.permissions.group.GroupsManager;
import me.moonways.bridgenet.model.permissions.permission.PermissionsManager;
import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.RemoteException;

public interface PermissionsServiceModel extends RemoteService {

    GroupsManager getGroups() throws RemoteException;

    PermissionsManager getPermissions() throws RemoteException;
}
