package me.moonways.service.event.subscribe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.service.event.EventFollowerImpl;
import me.moonways.services.api.events.BridgenetEventsService;
import me.moonways.services.api.events.event.Event;
import me.moonways.services.api.events.subscribe.EventSubscription;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class EventSubscriptionImpl<E extends Event> implements EventSubscription<E> {

    @Getter
    private final Class<E> eventType;

    private final EventSubscribeExpiration expiration;
    private final EventSubscribePredication<E> predication;

    @Getter
    private final EventFollowerImpl<E> follower;

    public void followExpiration(@NotNull BridgenetEventsService eventService) {
        if (expiration == null || expiration.isTimeoutExpired())
            return;

        follower.follow(event -> {

            if (expiration.isTimeoutExpired()) {
                eventService.unsubscribe(this);
            }
        });
    }

    // todo
    public boolean predicate(@NotNull E event) {
        return predication.getPredicate().test(event);
    }
}
