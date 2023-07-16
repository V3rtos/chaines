package me.moonways.service.api.events;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface EventFuture<E extends Event> {

    EventFuture<E> follow(@NotNull Consumer<E> eventConsumer);

    EventFuture<E> setTimeout(long timeout, @Nullable Runnable timeoutRunnable);

    EventFuture<E> setTimeout(long timeout);

    void complete(@NotNull E event);
}
