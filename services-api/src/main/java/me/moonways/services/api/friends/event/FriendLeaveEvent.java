package me.moonways.services.api.friends.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.services.api.events.event.Event;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class FriendLeaveEvent implements Event {

    private final String playerName;
    private final String leavedFriendName;
}
