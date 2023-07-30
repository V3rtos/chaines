package me.moonways.model.friends;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public interface FriendsList extends Remote {

    boolean addFriend(UUID uuid) throws RemoteException;

    boolean addFriend(String name) throws RemoteException;

    boolean removeFriend(UUID uuid) throws RemoteException;

    boolean removeFriend(String name) throws RemoteException;

    boolean hasFriend(UUID uuid) throws RemoteException;

    boolean hasFriend(String name) throws RemoteException;

    Set<UUID> getFriendsUUIDs() throws RemoteException;

    Set<String> getFriendsNames() throws RemoteException;
}
