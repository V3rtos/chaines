package me.moonways.services.api.events.subscribe;

import me.moonways.services.api.events.BridgenetEventsService;
import me.moonways.services.api.events.event.Event;
import me.moonways.services.api.events.follower.EventFollower;
import org.jetbrains.annotations.NotNull;

public interface EventSubscription<E extends Event> {

    EventFollower<E> getFollower();

    Class<E> getEventType();

    void followExpiration(@NotNull BridgenetEventsService eventService);

    boolean predicate(@NotNull E event);
}
