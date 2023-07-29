package me.moonways.service.api.friends;

import java.io.Serializable;
import java.util.UUID;
import java.util.stream.Stream;

public interface PlayerFriends extends Serializable {

    boolean addFriend(UUID uuid);

    boolean addFriend(String name);

    boolean removeFriend(UUID uuid);

    boolean removeFriend(String name);

    boolean hasFriend(UUID uuid);

    boolean hasFriend(String name);

    Stream<UUID> getFriendsUUIDs();
}
