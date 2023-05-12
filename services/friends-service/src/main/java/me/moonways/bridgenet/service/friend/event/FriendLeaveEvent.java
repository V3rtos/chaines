package me.moonways.bridgenet.service.friend.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.service.event.Event;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class FriendLeaveEvent implements Event {

    private final String playerName;
    private final String leavedFriendName;
}
