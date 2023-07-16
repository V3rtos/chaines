package me.moonways.service.api.friends.event;

import me.moonways.service.api.events.Event;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class FriendJoinEvent implements Event {

    private final String playerName;
    private final String joinedFriendName;
}
