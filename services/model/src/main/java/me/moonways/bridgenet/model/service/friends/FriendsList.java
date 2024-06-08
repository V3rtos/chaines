package me.moonways.bridgenet.model.service.friends;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.UUID;

public interface FriendsList extends Remote {

    boolean addFriend(UUID uuid) throws RemoteException;

    boolean addFriend(String name) throws RemoteException;

    boolean removeFriend(UUID uuid) throws RemoteException;

    boolean removeFriend(String name) throws RemoteException;

    boolean hasFriend(UUID uuid) throws RemoteException;

    boolean hasFriend(String name) throws RemoteException;

    Set<UUID> getFriendsIDs() throws RemoteException;

    Set<String> getFriendsNames() throws RemoteException;
}
