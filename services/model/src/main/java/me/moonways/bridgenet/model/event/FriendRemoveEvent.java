package me.moonways.bridgenet.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.service.friends.FriendsList;
import me.moonways.bridgenet.model.service.players.OfflinePlayer;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class FriendRemoveEvent implements Event {

    private final OfflinePlayer player;
    private final OfflinePlayer friend;
    private final FriendsList updatedFriendsList;
}
