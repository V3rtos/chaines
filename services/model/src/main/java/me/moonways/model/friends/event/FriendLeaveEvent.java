package me.moonways.model.friends.event;

import me.moonways.bridgenet.api.event.Event;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class FriendLeaveEvent implements Event {

    private final String playerName;
    private final String leavedFriendName;
}
