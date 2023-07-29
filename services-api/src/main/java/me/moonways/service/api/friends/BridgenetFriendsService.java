package me.moonways.service.api.friends;

import me.moonways.bridgenet.rsi.service.RemoteService;
import me.moonways.service.api.friends.event.FriendJoinEvent;
import me.moonways.service.api.friends.event.FriendLeaveEvent;

import java.rmi.RemoteException;
import java.util.UUID;
import java.util.function.Consumer;

public interface BridgenetFriendsService extends RemoteService {

    PlayerFriends findFriends(UUID playerUUID) throws RemoteException;

    PlayerFriends findFriends(String playerName) throws RemoteException;

    void cleanup(UUID playerUUID) throws RemoteException;

    void cleanup(String playerName) throws RemoteException;

    BridgenetFriendsService subscribeJoin(Consumer<FriendJoinEvent> eventHandler);

    BridgenetFriendsService subscribeLeave(Consumer<FriendLeaveEvent> eventHandler);
}
