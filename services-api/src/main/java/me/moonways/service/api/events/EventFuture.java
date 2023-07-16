package me.moonways.service.api.events;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.function.Consumer;

public interface EventFuture<E extends Event> extends Remote {

    EventFuture<E> follow(@NotNull Consumer<E> eventConsumer) throws RemoteException;

    EventFuture<E> setTimeout(long timeout, @Nullable Runnable timeoutRunnable) throws RemoteException;

    EventFuture<E> setTimeout(long timeout) throws RemoteException;

    void complete(@NotNull E event) throws RemoteException;
}
