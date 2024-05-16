package me.moonways.bridgenet.model.permissions.permission;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PermissionsManager extends Remote {

    Set<Permission> getActivePermissions(String playerName) throws RemoteException;

    Set<Permission> getActivePermissions(UUID playerId) throws RemoteException;

    Optional<PlayerPermissionPutEvent> addPermission(String playerName, Permission permission) throws RemoteException;

    Optional<PlayerPermissionPutEvent> addPermission(UUID playerId, Permission permission) throws RemoteException;

    Optional<PlayerPermissionPutEvent> addPermission(String playerName, String permissionName) throws RemoteException;

    Optional<PlayerPermissionPutEvent> addPermission(UUID playerId, String permissionName) throws RemoteException;

    Optional<PlayerPermissionRemoveEvent> removePermission(String playerName, Permission permission) throws RemoteException;

    Optional<PlayerPermissionRemoveEvent> removePermission(UUID playerId, Permission permission) throws RemoteException;

    Optional<PlayerPermissionRemoveEvent> removePermission(String playerName, String permissionName) throws RemoteException;

    Optional<PlayerPermissionRemoveEvent> removePermission(UUID playerId, String permissionName) throws RemoteException;

    void clearPermissions(String playerName) throws RemoteException;

    void clearPermissions(UUID playerId) throws RemoteException;

    boolean hasPermission(String playerName, Permission permission) throws RemoteException;

    boolean hasPermission(UUID playerId, Permission permission) throws RemoteException;

    boolean hasPermission(String playerName, String permissionName) throws RemoteException;

    boolean hasPermission(UUID playerId, String permissionName) throws RemoteException;
}
