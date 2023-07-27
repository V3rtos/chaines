package me.moonways.service.event;

import me.moonways.bridgenet.api.inject.Component;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.service.event.subscribe.EventSubscriptionApplier;
import me.moonways.service.api.events.BridgenetEventsService;
import me.moonways.service.api.events.Event;
import me.moonways.service.api.events.subscribe.EventSubscription;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public final class EventService extends UnicastRemoteObject implements BridgenetEventsService {

    private static final long serialVersionUID = 6386120101311957390L;

    private final ExecutorService threadsExecutorService = Executors.newCachedThreadPool();

    private final EventRegistry eventRegistry = new EventRegistry();

    private final EventExecutor eventExecutor = new EventExecutor(threadsExecutorService, eventRegistry);

    private final EventSubscriptionApplier eventSubscriptionApplier = new EventSubscriptionApplier(this);

    @Inject
    private DependencyInjection dependencyInjection;

    public EventService() throws RemoteException {
        super();
    }

    @NotNull
    public <E extends Event> EventFutureImpl<E> fireEvent(@NotNull E event) {
        dependencyInjection.injectFields(event);

        EventFutureImpl<E> eventFutureImpl = eventExecutor.fireEvent(event);
        eventSubscriptionApplier.followSubscription(eventFutureImpl);

        return eventFutureImpl;
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
