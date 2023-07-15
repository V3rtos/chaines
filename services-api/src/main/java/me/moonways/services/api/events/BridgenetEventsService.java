package me.moonways.services.api.events;

import me.moonways.bridgenet.rsi.service.RemoteService;
import me.moonways.services.api.events.event.Event;
import me.moonways.services.api.events.event.EventFuture;
import me.moonways.services.api.events.subscribe.EventSubscription;
import org.jetbrains.annotations.NotNull;

public interface BridgenetEventsService extends RemoteService {

    <E extends Event> EventFuture<E> fireEvent(@NotNull E event);

    void registerHandler(@NotNull Object handler);

    void unregisterHandler(@NotNull Object handler);

    void unregisterHandler(@NotNull Class<?> handlerType);

    void subscribe(@NotNull EventSubscription<?> subscription);

    void unsubscribe(@NotNull EventSubscription<?> subscription);
}
