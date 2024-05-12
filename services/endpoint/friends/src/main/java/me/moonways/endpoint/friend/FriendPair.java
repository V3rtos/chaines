package me.moonways.endpoint.friend;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.entity.persistence.DatabaseEntity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityParameter;

import java.util.UUID;

@Builder
@ToString
@DatabaseEntity(name = "friends")
public class FriendPair {

    @Getter(onMethod_ = @EntityParameter(id = "player_id", order = 1))
    private UUID playerID;

    @Getter(onMethod_ = @EntityParameter(id = "friend_id", order = 2))
    private UUID friendID;
}
