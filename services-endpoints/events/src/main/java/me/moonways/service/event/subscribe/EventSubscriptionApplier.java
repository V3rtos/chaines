package me.moonways.service.event.subscribe;

import lombok.RequiredArgsConstructor;
import me.moonways.service.event.*;
import me.moonways.services.api.events.event.Event;
import me.moonways.services.api.events.exception.EventException;

import java.util.Set;

@RequiredArgsConstructor
public final class EventSubscriptionApplier {

    private final EventService eventService;

    private final EventSubscriptionContainer container = new EventSubscriptionContainer();

    private void validateNull(EventSubscriptionImpl<?> subscription) {
        if (subscription == null) {
            throw new EventException("subscription is null");
        }
    }

    @SuppressWarnings("unchecked")
    public <E extends Event> void followSubscription(EventFutureImpl<E> future) {
        future.follow(event -> {

            Set<EventSubscriptionImpl<?>> subscriptionsAll = container.getSubscriptions(event.getClass());

            if (subscriptionsAll != null) {
                for (EventSubscriptionImpl<?> eventSubscriptionImpl : subscriptionsAll) {

                    EventSubscriptionImpl<E> genericSubscription = (EventSubscriptionImpl<E>) eventSubscriptionImpl;
                    genericSubscription.followExpiration(eventService);

                    EventFollower<E> follower = genericSubscription.getFollower();
                    follower.postComplete(event);
                }
            }
        });
    }

    public void subscribe(EventSubscriptionImpl<?> subscription) {
        validateNull(subscription);
        container.addSubscription(subscription);
    }

    public void unsubscribe(EventSubscriptionImpl<?> subscription) {
        validateNull(subscription);
        container.removeSubscription(subscription);
    }
}
