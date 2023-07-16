package me.moonways.service.api.events.subscribe;

import me.moonways.service.api.events.Event;
import me.moonways.service.api.events.BridgenetEventsService;
import me.moonways.service.api.events.follower.EventFollower;
import org.jetbrains.annotations.NotNull;

public interface EventSubscription<E extends Event> {

    EventFollower<E> getFollower();

    Class<E> getEventType();

    void followExpiration(@NotNull BridgenetEventsService eventService);

    boolean predicate(@NotNull E event);
}
