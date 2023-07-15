package me.moonways.service.event;

import me.moonways.bridgenet.injection.Component;
import me.moonways.service.event.subscribe.EventSubscription;
import me.moonways.service.event.subscribe.EventSubscriptionApplier;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.services.api.events.Event;
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
