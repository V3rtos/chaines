package me.moonways.bridgenet.api.event.subscribe;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.event.EventFollower;
import me.moonways.bridgenet.api.event.EventFuture;
import me.moonways.bridgenet.api.event.EventManager;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.api.event.EventException;

import java.util.Set;

@RequiredArgsConstructor
public final class EventSubscriptionApplier {

    private final EventManager eventManager;

    private final EventSubscriptionContainer container = new EventSubscriptionContainer();

    private void validateNull(EventSubscription<?> subscription) {
        if (subscription == null) {
            throw new EventException("subscription is null");
        }
    }

    @SuppressWarnings("unchecked")
    public <E extends Event> void followSubscription(EventFuture<E> future) {
        future.follow(event -> {

            Set<EventSubscription<?>> subscriptionsAll = container.getSubscriptions(event.getClass());

            if (subscriptionsAll != null) {
                for (EventSubscription<?> eventSubscriptionImpl : subscriptionsAll) {

                    EventSubscription<E> genericSubscription = (EventSubscription<E>) eventSubscriptionImpl;
                    genericSubscription.followExpiration(eventManager);

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
