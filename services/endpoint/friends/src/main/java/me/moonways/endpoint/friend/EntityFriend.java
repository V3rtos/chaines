package me.moonways.endpoint.friend;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.entity.persistence.Entity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityColumn;

import java.util.UUID;

@Builder
@ToString
@Entity(name = "friends")
public class EntityFriend {

    @Getter(onMethod_ = @EntityColumn(id = "player_id", order = 1))
    private UUID playerID;

    @Getter(onMethod_ = @EntityColumn(id = "friend_id", order = 2))
    private UUID friendID;
}
