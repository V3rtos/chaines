package me.moonways.endpoint.friend;

import lombok.Getter;
import me.moonways.bridgenet.api.event.EventManager;
import me.moonways.bridgenet.api.event.subscribe.EventSubscribeBuilder;
import me.moonways.bridgenet.api.inject.Component;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostFactoryMethod;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.model.friends.FriendsList;
import me.moonways.model.friends.FriendsServiceModel;
import me.moonways.model.friends.event.FriendJoinEvent;
import me.moonways.model.friends.event.FriendLeaveEvent;
import me.moonways.model.players.PlayersServiceModel;
import net.conveno.jdbc.ConvenoRouter;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public final class FriendsServiceEndpoint extends AbstractEndpointDefinition implements FriendsServiceModel {

    private static final long serialVersionUID = 6945343361490533671L;

    @Getter
    private FriendsDatabaseRepository repository;

    @Inject
    private ConvenoRouter convenoRouter;

    @Inject
    private EventManager eventManager;

    @Inject
    private PlayersServiceModel playersModel;

    private final Map<UUID, FriendsList> playerFriendsMap = new HashMap<>();

    private Consumer<FriendJoinEvent> friendJoinEventConsumer;
    private Consumer<FriendLeaveEvent> friendLeaveEventConsumer;

    public FriendsServiceEndpoint() throws RemoteException {
        super();
    }

    @PostFactoryMethod
    private void init() {
        repository = convenoRouter.getRepository(FriendsDatabaseRepository.class);
        repository.executeTableValid();

        eventManager.subscribe(EventSubscribeBuilder.newBuilder(FriendJoinEvent.class)
                .follow(friendJoinEventConsumer)
                .build());
        eventManager.subscribe(EventSubscribeBuilder.newBuilder(FriendLeaveEvent.class)
                .follow(friendLeaveEventConsumer)
                .build());
    }

    private FriendsList lookupPlayerFriends(UUID playerUUID) {
        return null;
    }

    @Override
    public FriendsList findFriends(UUID playerUUID) throws RemoteException {
        return playerFriendsMap.computeIfAbsent(playerUUID, this::lookupPlayerFriends);
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
        if (friendJoinEventConsumer == null) {
            friendJoinEventConsumer = eventHandler;
        }
        else friendJoinEventConsumer = friendJoinEventConsumer.andThen(eventHandler);
        return this;
    }

    @Override
    public FriendsServiceModel subscribeLeave(Consumer<FriendLeaveEvent> eventHandler) {
        if (friendLeaveEventConsumer == null) {
            friendLeaveEventConsumer = eventHandler;
        }
        else friendLeaveEventConsumer = friendLeaveEventConsumer.andThen(eventHandler);
        return this;
    }
}
