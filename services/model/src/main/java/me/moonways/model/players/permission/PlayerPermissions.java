package me.moonways.model.players.permission;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface PlayerPermissions extends Remote {

    PermissionGroup findGroupByID(int groupId) throws RemoteException;

    PermissionGroup findGroupByName(String groupName) throws RemoteException;

    PermissionGroup findGroupByPlayer(String playerName) throws RemoteException;

    PermissionGroup findGroupByPlayer(UUID playerUuid) throws RemoteException;

    String lookupChatPrefix(String playerName) throws RemoteException;

    String lookupChatPrefix(UUID playerUuid) throws RemoteException;

    String lookupTablistPrefix(String playerName) throws RemoteException;

    String lookupTablistPrefix(UUID playerUuid) throws RemoteException;

    boolean hasPermission(String playerName, String permission) throws RemoteException;

    boolean hasPermission(UUID playerUuid, String permission) throws RemoteException;

    boolean isDefaultPlayer(String playerName) throws RemoteException;

    boolean isDefaultPlayer(UUID playerUuid) throws RemoteException;

    boolean isStaffPlayer(String playerName) throws RemoteException;

    boolean isStaffPlayer(UUID playerUuid) throws RemoteException;

    boolean isGroupExpired(String playerName) throws RemoteException;

    boolean isGroupExpired(UUID playerUuid) throws RemoteException;
}
