package me.moonways.endpoint.friend;

import lombok.Getter;
import me.moonways.bridgenet.api.event.EventManager;
import me.moonways.bridgenet.api.event.subscribe.EventSubscribeBuilder;
import me.moonways.bridgenet.api.inject.Depend;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.model.friends.FriendsList;
import me.moonways.model.friends.FriendsServiceModel;
import me.moonways.model.friends.event.FriendJoinEvent;
import me.moonways.model.friends.event.FriendLeaveEvent;
import me.moonways.model.players.PlayersServiceModel;
import net.conveno.jdbc.ConvenoRouter;
import net.conveno.jdbc.response.ConvenoResponse;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@Depend
public final class FriendsServiceEndpoint extends AbstractEndpointDefinition implements FriendsServiceModel {

    private static final long serialVersionUID = 6945343361490533671L;

    private FriendsRepository repository;

    @Inject
    private ConvenoRouter convenoRouter;

    @Inject
    private EventManager eventManager;

    @Inject
    private PlayersServiceModel playersModel;

    private final Map<UUID, FriendsList> playerFriendsMap = new HashMap<>();

    private Consumer<FriendJoinEvent> friendJoinEventConsumer = (event) -> {};
    private Consumer<FriendLeaveEvent> friendLeaveEventConsumer = (event) -> {};

    public FriendsServiceEndpoint() throws RemoteException {
        super();
    }

    @PostConstruct
    private void init() {
        repository = convenoRouter.getRepository(FriendsRepository.class);
        repository.executeTableValid();

        eventManager.subscribe(EventSubscribeBuilder.newBuilder(FriendJoinEvent.class)
                .follow(friendJoinEventConsumer)
                .build());
        eventManager.subscribe(EventSubscribeBuilder.newBuilder(FriendLeaveEvent.class)
                .follow(friendLeaveEventConsumer)
                .build());
    }

    private FriendsList lookupPlayerFriends(UUID playerUUID) throws RemoteException {
        ConvenoResponse friendsListResponse = repository.findFriendsList(playerUUID);
        return new FriendsListStub(
                playerUUID,
                playersModel,
                repository,
                friendsListResponse.stream()
                        .map(line -> UUID.fromString(line.getNullableString("uuid")))
                        .collect(Collectors.toSet())
        );
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
