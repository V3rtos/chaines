package me.moonways.bridgenet.model.permissions.group;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupsManager extends Remote {

    List<PermissionGroup> getTotalGroups() throws RemoteException;

    List<PermissionGroup> getDonateGroups() throws RemoteException;

    List<PermissionGroup> getTemporalGroups() throws RemoteException;

    List<PermissionGroup> getCommercialGroups() throws RemoteException;

    List<PermissionGroup> getPersonalGroups() throws RemoteException;

    List<PermissionGroup> getTechPersonalGroups() throws RemoteException;

    List<PermissionGroup> getOwnerGroups() throws RemoteException;

    Optional<PermissionGroup> fromId(int groupId) throws RemoteException;

    Optional<PermissionGroup> fromName(String groupName) throws RemoteException;

    Optional<PermissionGroup> fromPlayerName(String playerName) throws RemoteException;

    Optional<PermissionGroup> fromPlayerId(UUID playerId) throws RemoteException;

    Optional<PlayerGroupUpdateEvent> updatePlayer(String playerName, PermissionGroup group) throws RemoteException;

    Optional<PlayerGroupUpdateEvent> updatePlayer(UUID playerId, PermissionGroup group) throws RemoteException;

    PermissionGroup getDefault() throws RemoteException;

    boolean isDefault(String playerName) throws RemoteException;

    boolean isDefault(UUID playerId) throws RemoteException;

    boolean isDonated(String playerName) throws RemoteException;

    boolean isDonated(UUID playerId) throws RemoteException;

    boolean isPersonal(String playerName) throws RemoteException;

    boolean isPersonal(UUID playerId) throws RemoteException;

    boolean isTechPersonal(String playerName) throws RemoteException;

    boolean isTechPersonal(UUID playerId) throws RemoteException;

    boolean isOwner(String playerName) throws RemoteException;

    boolean isOwner(UUID playerId) throws RemoteException;
}
