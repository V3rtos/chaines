package me.moonways.endpoint.friend;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.jdbc.entity.EntityRepositoryFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FriendsDbRepository {

    @Inject
    private EntityRepositoryFactory repositoryFactory;

    private EntityRepository<FriendPair> friendPairEntityDao;

    public EntityRepository<FriendPair> getRepository() {
        if (friendPairEntityDao == null) {
            friendPairEntityDao = repositoryFactory.fromEntityType(FriendPair.class);
        }
        return friendPairEntityDao;
    }

    public List<UUID> findFriendsList(UUID playerID) {
        EntityRepository<FriendPair> friendsPairRepository = getRepository();
        return friendsPairRepository.searchManyIf(friendsPairRepository.newSearchMarker()
                        .withGet(FriendPair::getPlayerID, playerID))
                .stream()
                .map(FriendPair::getFriendID)
                .collect(Collectors.toList());
    }

    public void addFriend(FriendPair pair) {
        EntityRepository<FriendPair> friendsPairRepository = getRepository();
        friendsPairRepository.insertMany(pair, reverse(pair));
    }

    public void removeFriend(FriendPair pair) {
        EntityRepository<FriendPair> friendsPairRepository = getRepository();
        friendsPairRepository.deleteMany(pair, reverse(pair));
    }

    private FriendPair reverse(FriendPair pair) {
        return FriendPair.builder()
                .playerID(pair.getFriendID())
                .friendID(pair.getPlayerID())
                .build();
    }
}
