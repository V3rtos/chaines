package me.moonways.endpoint.friend;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.model.friends.FriendsList;
import me.moonways.bridgenet.model.friends.FriendsServiceModel;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Getter
@Autobind
public final class FriendsServiceEndpoint extends AbstractEndpointDefinition implements FriendsServiceModel {

    private static final long serialVersionUID = 6945343361490533671L;

    @Inject
    private EventService eventService;
    @Inject
    private BeansService beansService;
    @Inject
    private PlayersServiceModel playersModel;

    private final Cache<UUID, FriendsList> playerFriendsCache = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.HOURS)
            .build();

    private FriendsDbRepository repository;

    public FriendsServiceEndpoint() throws RemoteException {
        super();
    }

    @PostConstruct
    private void init() {
        repository = new FriendsDbRepository();
        beansService.inject(repository);
    }

    private FriendsList lookupPlayerFriends(UUID playerUUID) throws RemoteException {
        List<UUID> friendsList = repository.findFriendsList(playerUUID);
        return new FriendsListStub(
                playerUUID,
                playersModel,
                repository,
                new HashSet<>(friendsList));
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
        UUID playerId = playersModel.findPlayerId(playerName);
        return getFriends(playerId);
    }

}
