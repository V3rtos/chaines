package me.moonways.service.event.subscribe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.service.event.EventFollowerImpl;
import me.moonways.service.api.events.BridgenetEventsService;
import me.moonways.service.api.events.Event;
import me.moonways.service.api.events.subscribe.EventSubscription;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;

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
                try {
                    eventService.unsubscribe(this);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // todo
    public boolean predicate(@NotNull E event) {
        return predication.getPredicate().test(event);
    }
}
