package me.moonways.endpoint.friend;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.dao.EntityAccessCondition;
import me.moonways.bridgenet.jdbc.dao.EntityDao;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FriendsDbRepository {

    @Inject
    private DatabaseProvider provider;
    @Inject
    private DatabaseConnection connection;

    private EntityDao<FriendPair> friendPairEntityDao;

    public EntityDao<FriendPair> getDao() {
        if (friendPairEntityDao == null) {
            friendPairEntityDao = provider.createDao(FriendPair.class, connection);
        }
        return friendPairEntityDao;
    }

    public List<UUID> findFriendsList(UUID playerID) {
        EntityDao<FriendPair> friendPairDao = getDao();
        return friendPairDao.findMany(EntityAccessCondition.createMono("player_id", playerID))
                .stream()
                .map(FriendPair::getFriendID)
                .collect(Collectors.toList());
    }

    public void addFriend(FriendPair pair) {
        EntityDao<FriendPair> friendPairDao = getDao();
        friendPairDao.insertMany(pair, reverse(pair));
    }

    public void removeFriend(FriendPair pair) {
        EntityDao<FriendPair> friendPairDao = getDao();
        friendPairDao.deleteMany(pair, reverse(pair));
    }

    private FriendPair reverse(FriendPair pair) {
        return FriendPair.builder()
                .playerID(pair.getFriendID())
                .friendID(pair.getPlayerID())
                .build();
    }
}
