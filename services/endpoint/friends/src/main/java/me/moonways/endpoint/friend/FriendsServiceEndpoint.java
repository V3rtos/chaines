package me.moonways.endpoint.friend;

import lombok.Getter;
import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.event.subscribe.EventSubscribeBuilder;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.model.friends.FriendsList;
import me.moonways.bridgenet.model.friends.FriendsServiceModel;
import me.moonways.bridgenet.model.friends.event.FriendJoinEvent;
import me.moonways.bridgenet.model.friends.event.FriendLeaveEvent;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;

import java.rmi.RemoteException;
import java.util.*;
import java.util.function.Consumer;

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

    private final Map<UUID, FriendsList> playerFriendsMap = new HashMap<>();

    private Consumer<FriendJoinEvent> friendJoinEventConsumer = (event) -> {};
    private Consumer<FriendLeaveEvent> friendLeaveEventConsumer = (event) -> {};

    private FriendsDbRepository repository;

    public FriendsServiceEndpoint() throws RemoteException {
        super();
    }

    @PostConstruct
    private void init() {
        repository = new FriendsDbRepository();
        beansService.inject(repository);

        eventService.subscribe(EventSubscribeBuilder.newBuilder(FriendJoinEvent.class)
                .follow(friendJoinEventConsumer)
                .build());
        eventService.subscribe(EventSubscribeBuilder.newBuilder(FriendLeaveEvent.class)
                .follow(friendLeaveEventConsumer)
                .build());
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
    public FriendsList findFriends(UUID playerUUID) throws RemoteException {
        if (playerFriendsMap.containsKey(playerUUID)) {
            return playerFriendsMap.get(playerUUID);
        }

        FriendsList friendsList = lookupPlayerFriends(playerUUID);
        playerFriendsMap.put(playerUUID, friendsList);

        return friendsList;
    }

    @Override
    public FriendsList findFriends(String playerName) throws RemoteException {
        UUID playerId = playersModel.findPlayerId(playerName);
        return findFriends(playerId);
    }

    @Override
    public void cleanup(UUID playerUUID) throws RemoteException {
        playerFriendsMap.remove(playerUUID);
    }

    @Override
    public void cleanup(String playerName) throws RemoteException {
        UUID playerId = playersModel.findPlayerId(playerName);
        this.cleanup(playerId);
    }

    @Override
    public FriendsServiceModel subscribeJoin(Consumer<FriendJoinEvent> eventHandler) {
        friendJoinEventConsumer = friendJoinEventConsumer.andThen(eventHandler);
        return this;
    }

    @Override
    public FriendsServiceModel subscribeLeave(Consumer<FriendLeaveEvent> eventHandler) {
        friendLeaveEventConsumer = friendLeaveEventConsumer.andThen(eventHandler);
        return this;
    }
}
