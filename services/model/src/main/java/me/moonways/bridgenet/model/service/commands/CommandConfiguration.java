package me.moonways.bridgenet.model.service.commands;

import me.moonways.bridgenet.model.audience.EntityAudience;
import me.moonways.bridgenet.model.service.players.OfflinePlayer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public interface CommandConfiguration extends Remote {

    String getName() throws RemoteException;

    String[] getAliases() throws RemoteException;

    Optional<String> getUsage() throws RemoteException;

    Optional<String> getDescription() throws RemoteException;

    Optional<String> getPermission() throws RemoteException;

    Optional<String> getRegex() throws RemoteException;

    Optional<Long> getCooldown() throws RemoteException;

    List<EntityAudience> getAllowedEntities() throws RemoteException;

    boolean isOnlyConsole() throws RemoteException;

    boolean isOnlyUser() throws RemoteException;

    boolean isOnlyBoth() throws RemoteException;

    void grantConsoleAccess() throws RemoteException;

    void denyConsoleAccess() throws RemoteException;

    void grantUserAccess() throws RemoteException;

    void denyAccess() throws RemoteException;

    void grantAccess(OfflinePlayer offlinePlayer) throws RemoteException;

    void denyAccess(OfflinePlayer offlinePlayer) throws RemoteException;

    void grantAccess(EntityAudience entityAudience) throws RemoteException;

    void denyAccess(EntityAudience entityAudience);
}
