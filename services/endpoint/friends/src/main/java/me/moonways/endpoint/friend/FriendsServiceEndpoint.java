package me.moonways.endpoint.friend;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.friends.FriendsList;
import me.moonways.bridgenet.model.friends.FriendsServiceModel;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteContext;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;
import me.moonways.endpoint.friend.event.FriendActivityListener;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Getter
public final class FriendsServiceEndpoint extends EndpointRemoteObject implements FriendsServiceModel {

    private static final long serialVersionUID = 6945343361490533671L;

    private final Cache<UUID, FriendsList> playerFriendsCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    private final FriendsDbRepository repository = new FriendsDbRepository();

    @Inject
    private PlayersServiceModel playersServiceModel;

    public FriendsServiceEndpoint() throws RemoteException {
        super();
    }

    @Override
    protected void construct(EndpointRemoteContext context) {
        context.registerEventListener(new FriendActivityListener());
    }

    private FriendsList lookupPlayerFriends(UUID playerUUID) throws RemoteException {
        List<UUID> friendsList = repository.findFriendsList(playerUUID);
        FriendsListStub friendsListStub = new FriendsListStub(
                playerUUID,
                repository,
                new HashSet<>(friendsList));

        getEndpointContext().inject(friendsListStub);

        return friendsListStub;
    }

    @Override
    public FriendsList getFriends(UUID playerUUID) throws RemoteException {
        playerFriendsCache.cleanUp();
        ConcurrentMap<UUID, FriendsList> cacheMap = playerFriendsCache.asMap();

        if (cacheMap.containsKey(playerUUID)) {
            return cacheMap.get(playerUUID);
        }

        FriendsList friendsList = lookupPlayerFriends(playerUUID);
        playerFriendsCache.put(playerUUID, friendsList);

        return friendsList;
    }

    @Override
    public FriendsList getFriends(String playerName) throws RemoteException {
        UUID playerId = playersServiceModel.store().idByName(playerName);
        return getFriends(playerId);
    }
}
