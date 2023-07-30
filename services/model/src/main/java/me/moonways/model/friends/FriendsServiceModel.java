package me.moonways.model.friends;

import me.moonways.model.friends.event.FriendJoinEvent;
import me.moonways.model.friends.event.FriendLeaveEvent;
import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.RemoteException;
import java.util.UUID;
import java.util.function.Consumer;

public interface FriendsServiceModel extends RemoteService {

    FriendsList findFriends(UUID playerUUID) throws RemoteException;

    FriendsList findFriends(String playerName) throws RemoteException;

    void cleanup(UUID playerUUID) throws RemoteException;

    void cleanup(String playerName) throws RemoteException;

    FriendsServiceModel subscribeJoin(Consumer<FriendJoinEvent> eventHandler) throws RemoteException;

    FriendsServiceModel subscribeLeave(Consumer<FriendLeaveEvent> eventHandler) throws RemoteException;
}
