package me.moonways.services.api.events.subscribe;

import me.moonways.services.api.events.BridgenetEventsService;
import me.moonways.services.api.events.event.Event;
import org.jetbrains.annotations.NotNull;

public interface EventSubscription<E extends Event> {

    Class<E> getEventType();

    void followExpiration(@NotNull BridgenetEventsService eventService);

    boolean predicate(@NotNull E event);
}
