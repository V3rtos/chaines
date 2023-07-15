package me.moonways.service.event.subscribe;

import me.moonways.services.api.events.event.Event;

import java.util.*;

public class EventSubscriptionContainer {

    private final Map<Class<? extends Event>, Set<EventSubscriptionImpl<?>>> subscriptionMap = Collections.synchronizedMap(new HashMap<>());

    public void addSubscription(EventSubscriptionImpl<?> subscription) {
        registerInternal(subscription);
    }

    public void removeSubscription(EventSubscriptionImpl<?> subscription) {
        subscriptionMap.remove(subscription.getEventType());
    }

    public Set<EventSubscriptionImpl<?>> getSubscriptions(Class<? extends Event> eventType) {
        return subscriptionMap.get(eventType);
    }

    private void registerInternal(EventSubscriptionImpl<?> subscription) {
        Class<? extends Event> eventType = subscription.getEventType();
        Set<EventSubscriptionImpl<?>> eventSubscriptionImpls = subscriptionMap.get(eventType);

        if (eventSubscriptionImpls == null)
            eventSubscriptionImpls = new HashSet<>();

        eventSubscriptionImpls.add(subscription);

        subscriptionMap.put(eventType, eventSubscriptionImpls);
    }
}
