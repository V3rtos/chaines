package me.moonways.bridgenet.api.event;

import me.moonways.bridgenet.api.inject.Component;
import me.moonways.bridgenet.api.event.subscribe.EventSubscription;
import me.moonways.bridgenet.api.event.subscribe.EventSubscriptionApplier;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public final class EventService {

    private final ExecutorService threadsExecutorService = Executors.newCachedThreadPool();

// ----------------------------------------------------------------------------------------------------- //

    private final EventRegistry eventRegistry = new EventRegistry();

    private final EventExecutor eventExecutor = new EventExecutor(threadsExecutorService, eventRegistry);

    private final EventSubscriptionApplier eventSubscriptionApplier = new EventSubscriptionApplier(this);

// ----------------------------------------------------------------------------------------------------- //

    @NotNull
    public <E extends Event> EventFuture<E> fireEvent(@NotNull E event) {
        EventFuture<E> eventFuture = eventExecutor.fireEvent(event);
        eventSubscriptionApplier.followSubscription(eventFuture);

        return eventFuture;
    }

    public void registerHandler(@NotNull Object handler) {
        eventRegistry.register(handler);
    }

    public void unregisterHandler(@NotNull Object handler) {
        eventRegistry.unregister(handler.getClass());
    }

    public void unregisterHandler(@NotNull Class<?> handlerType) {
        eventRegistry.unregister(handlerType);
    }

    public void subscribe(@NotNull EventSubscription<?> subscription) {
        eventSubscriptionApplier.subscribe(subscription);
    }

    public void unsubscribe(@NotNull EventSubscription<?> subscription) {
        eventSubscriptionApplier.unsubscribe(subscription);
    }
}
