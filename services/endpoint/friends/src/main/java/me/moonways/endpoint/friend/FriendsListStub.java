package me.moonways.endpoint.friend;

import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.model.friends.FriendsList;
import me.moonways.model.players.PlayersServiceModel;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class FriendsListStub extends AbstractEndpointDefinition implements FriendsList {

    private static final long serialVersionUID = 8227026529064476222L;

    private final PlayersServiceModel playersModel;
    private final Set<UUID> uuids;

    public FriendsListStub(PlayersServiceModel playersModel, Set<UUID> uuids) throws RemoteException {
        super();
        this.playersModel = playersModel;
        this.uuids = uuids;
    }

    @Override
    public boolean addFriend(UUID uuid) {
        return false;
    }

    @Override
    public boolean addFriend(String name) {
        return false;
    }

    @Override
    public boolean removeFriend(UUID uuid) {
        return false;
    }

    @Override
    public boolean removeFriend(String name) {
        return false;
    }

    @Override
    public boolean hasFriend(UUID uuid) {
        return false;
    }

    @Override
    public boolean hasFriend(String name) {
        return false;
    }

    @Override
    public Stream<UUID> getFriendsUUIDs() {
        return uuids.stream();
    }
}
