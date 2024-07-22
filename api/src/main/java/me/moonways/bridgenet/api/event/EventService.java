package me.moonways.bridgenet.api.event;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.event.subscribe.EventSubscription;
import me.moonways.bridgenet.api.event.subscribe.EventSubscriptionApplier;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.processor.ScanningResult;
import me.moonways.bridgenet.api.inject.processor.persistence.AwaitAnnotationsScanning;
import me.moonways.bridgenet.api.inject.processor.persistence.GetAnnotationsScanningResult;
import me.moonways.bridgenet.api.util.thread.Threads;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;

@Autobind
@Log4j2
@AwaitAnnotationsScanning(InboundEventListener.class)
public final class EventService {

    private final ExecutorService forkJoinPool = Threads.newWorkStealingPool();

    private final EventRegistry eventRegistry = new EventRegistry();
    private final EventExecutor eventExecutor = new EventExecutor(forkJoinPool, eventRegistry);

    private final EventSubscriptionApplier eventSubscriptionApplier = new EventSubscriptionApplier(this);

    @Inject
    private BeansService beansService;

    @GetAnnotationsScanningResult
    private ScanningResult<Object> listenersResult;

    @PostConstruct
    private void registerInboundListeners() {
        listenersResult.toList().forEach(this::registerListener);
    }

    @NotNull
    public synchronized <E extends Event> EventFuture<E> fireEvent(@NotNull E event) {
        EventFuture<E> eventFuture = eventExecutor.fireEvent(event);
        eventSubscriptionApplier.followSubscription(eventFuture);

        log.debug("Event: §7{}", event);
        return eventFuture;
    }

    public void registerListener(@NotNull Object listener) {
        log.debug("Listening inbound events started on §6{}", listener.getClass().getName());

        beansService.inject(listener);
        eventRegistry.register(listener);
    }

    public void unregisterListener(@NotNull Object listener) {
        unregisterListener(listener.getClass());
    }

    public void unregisterListener(@NotNull Class<?> listenerClass) {
        log.debug("Listening inbound events shutdown for §4{}", listenerClass.getName());
        eventRegistry.unregister(listenerClass);
    }

    public void subscribe(@NotNull EventSubscription<?> subscription) {
        eventSubscriptionApplier.subscribe(subscription);
    }

    public void unsubscribe(@NotNull EventSubscription<?> subscription) {
        eventSubscriptionApplier.unsubscribe(subscription);
    }
}
