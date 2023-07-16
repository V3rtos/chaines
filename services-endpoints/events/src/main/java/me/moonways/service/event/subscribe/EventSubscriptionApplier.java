package me.moonways.service.event.subscribe;

import lombok.RequiredArgsConstructor;
import me.moonways.service.event.*;
import me.moonways.service.api.events.Event;
import me.moonways.service.api.events.exception.EventException;
import me.moonways.service.api.events.follower.EventFollower;
import me.moonways.service.api.events.subscribe.EventSubscription;

import java.util.Set;

@RequiredArgsConstructor
public final class EventSubscriptionApplier {

    private final EventService eventService;

    private final EventSubscriptionContainer container = new EventSubscriptionContainer();

    private void validateNull(EventSubscription<?> subscription) {
        if (subscription == null) {
            throw new EventException("subscription is null");
        }
    }

    @SuppressWarnings("unchecked")
    public <E extends Event> void followSubscription(EventFutureImpl<E> future) {
        future.follow(event -> {

            Set<EventSubscription<?>> subscriptionsAll = container.getSubscriptions(event.getClass());

            if (subscriptionsAll != null) {
                for (EventSubscription<?> eventSubscriptionImpl : subscriptionsAll) {

                    EventSubscription<E> genericSubscription = (EventSubscription<E>) eventSubscriptionImpl;
                    genericSubscription.followExpiration(eventService);

                    EventFollower<E> follower = genericSubscription.getFollower();
                    follower.postComplete(event);
                }
            }
        });
    }

    public void subscribe(EventSubscription<?> subscription) {
        validateNull(subscription);
        container.addSubscription(subscription);
    }

    public void unsubscribe(EventSubscription<?> subscription) {
        validateNull(subscription);
        container.removeSubscription(subscription);
    }
}
