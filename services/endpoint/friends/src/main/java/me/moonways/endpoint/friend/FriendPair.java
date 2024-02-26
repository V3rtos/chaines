package me.moonways.endpoint.friend;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.dao.entity.EntityAccessible;
import me.moonways.bridgenet.jdbc.dao.entity.EntityElement;

import java.util.UUID;

@Getter
@Builder
@ToString
@EntityAccessible(name = "friends")
public class FriendPair {

    @EntityElement(id = "player_id", order = 1)
    private UUID playerID;

    @EntityElement(id = "friend_id", order = 2)
    private UUID friendID;
}
