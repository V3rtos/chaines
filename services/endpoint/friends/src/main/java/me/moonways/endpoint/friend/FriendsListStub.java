package me.moonways.endpoint.friend;

import lombok.ToString;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.bridgenet.model.friends.FriendsList;
import me.moonways.bridgenet.model.players.PlayersServiceModel;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ToString(onlyExplicitlyIncluded = true)
public class FriendsListStub extends AbstractEndpointDefinition implements FriendsList {

    private static final long serialVersionUID = 8227026529064476222L;

    @ToString.Include
    private final UUID playerUUID;

    private final PlayersServiceModel playersModel;
    private final FriendsDbRepository repository;

    @ToString.Include
    private final Set<UUID> uuids;

    public FriendsListStub(UUID playerUUID, PlayersServiceModel playersModel, FriendsDbRepository repository, Set<UUID> uuids) throws RemoteException {
        super();

        this.playerUUID = playerUUID;
        this.playersModel = playersModel;
        this.repository = repository;
        this.uuids = uuids;
    }

    @Override
    public boolean addFriend(UUID uuid) {
        boolean add = uuids.add(uuid);
        if (add) {
            repository.addFriend(FriendPair.builder()
                    .playerID(playerUUID)
                    .friendID(uuid)
                    .build());
        }
        return add;
    }

    @Override
    public boolean addFriend(String name) throws RemoteException {
        return addFriend(playersModel.findPlayerId(name));
    }

    @Override
    public boolean removeFriend(UUID uuid) {
        boolean add = uuids.remove(uuid);
        if (add) {
            repository.removeFriend(FriendPair.builder()
                    .playerID(playerUUID)
                    .friendID(uuid)
                    .build());
        }
        return add;
    }

    @Override
    public boolean removeFriend(String name) throws RemoteException {
        return removeFriend(playersModel.findPlayerId(name));
    }

    @Override
    public boolean hasFriend(UUID uuid) {
        return uuids.contains(uuid);
    }

    @Override
    public boolean hasFriend(String name) throws RemoteException {
        return hasFriend(playersModel.findPlayerId(name));
    }

    @Override
    public Set<UUID> getFriendsIDs() {
        return uuids;
    }

    @Override
    public Set<String> getFriendsNames() throws RemoteException {
        return uuids.stream().map(uuid -> {
                    try {
                        return playersModel.findPlayerName(uuid);
                    } catch (RemoteException exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .collect(Collectors.toSet());
    }
}
