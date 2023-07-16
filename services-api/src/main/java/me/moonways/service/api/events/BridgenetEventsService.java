package me.moonways.service.api.events;

import me.moonways.service.api.events.subscribe.EventSubscription;
import me.moonways.bridgenet.rsi.service.RemoteService;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;

public interface BridgenetEventsService extends RemoteService {

    <E extends Event> EventFuture<E> fireEvent(@NotNull E event) throws RemoteException;

    void registerHandler(@NotNull Object handler) throws RemoteException;

    void unregisterHandler(@NotNull Object handler) throws RemoteException;

    void unregisterHandler(@NotNull Class<?> handlerType) throws RemoteException;

    void subscribe(@NotNull EventSubscription<?> subscription) throws RemoteException;

    void unsubscribe(@NotNull EventSubscription<?> subscription) throws RemoteException;
}
