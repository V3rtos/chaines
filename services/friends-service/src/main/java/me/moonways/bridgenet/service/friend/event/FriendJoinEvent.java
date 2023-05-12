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
public class FriendJoinEvent implements Event {

    private final String playerName;
    private final String joinedFriendName;
}
