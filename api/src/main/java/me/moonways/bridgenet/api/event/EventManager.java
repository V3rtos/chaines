package me.moonways.bridgenet.api.event;

import me.moonways.bridgenet.api.event.subscribe.EventSubscription;
import me.moonways.bridgenet.api.event.subscribe.EventSubscriptionApplier;
import me.moonways.bridgenet.api.inject.Depend;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.thread.Threads;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Depend
public final class EventManager {

    private final ExecutorService threadsExecutorService = Threads.newCachedThreadPool();

    private final EventRegistry eventRegistry = new EventRegistry();

    private final EventExecutor eventExecutor = new EventExecutor(threadsExecutorService, eventRegistry);

    private final EventSubscriptionApplier eventSubscriptionApplier = new EventSubscriptionApplier(this);

    @Inject
    private DependencyInjection dependencyInjection;

    @NotNull
    public <E extends Event> EventFuture<E> fireEvent(@NotNull E event) {
        dependencyInjection.injectFields(event);

        EventFuture<E> eventFuture = eventExecutor.fireEvent(event);
        eventSubscriptionApplier.followSubscription(eventFuture);

        return eventFuture;
    }

    public void registerHandler(@NotNull Object handler) {
        dependencyInjection.injectFields(handler);
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
