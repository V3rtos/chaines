package me.moonways.bridgenet.model.players;

import me.moonways.bridgenet.model.players.permission.PermissionGroup;
import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.UUID;

public interface PlayerStatistic extends Remote {

    void setProperty(@NotNull String property, int value) throws RemoteException;

    int getProperty(@NotNull String property) throws RemoteException;

    int getCurrentLevel() throws RemoteException;

    int getNextLevel() throws RemoteException;

    int getTotalLevelExperience() throws RemoteException;

    int getCurrentExperience() throws RemoteException;

    int getExperienceLeft() throws RemoteException;

    Date getRegistrationDate() throws RemoteException;

    Date getLastLoginedDate() throws RemoteException;

    UUID getUniqueId() throws RemoteException;

    String getName() throws RemoteException;

    String getChatDisplay() throws RemoteException;

    String getTablistDisplay() throws RemoteException;

    String getLastConnectedServerName() throws RemoteException;

    PermissionGroup getStoredGroup() throws RemoteException;

    PermissionGroup getActualGroup() throws RemoteException;

    boolean hasPermission(@NotNull String permission) throws RemoteException;

    boolean isOnline() throws RemoteException;
}
