package me.moonways.service.event;

import me.moonways.service.api.events.Event;
import me.moonways.service.api.events.exception.EventException;
import me.moonways.service.api.events.follower.EventFollower;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class EventFollowerImpl<E extends Event> implements EventFollower<E> {

    private final Object lock = new Object();

    private Consumer<E> thenExecuteConsumer;
    private E completed;

    private void validateNull(Event event) {
        if (event == null) {
            throw new EventException("event is null");
        }
    }

    @Override
    public boolean isCompleted() {
        return completed != null;
    }

    @Override
    public void follow(@Nullable Consumer<E> eventConsumer) {
        if (eventConsumer == null)
            return;

        synchronized (lock) {
            if (isCompleted()) {
                eventConsumer.accept(completed);
                return;
            }

            if (thenExecuteConsumer == null)
                thenExecuteConsumer = eventConsumer;
            else
                thenExecuteConsumer = thenExecuteConsumer.andThen(eventConsumer);
        }
    }

    @Override
    public void postComplete(@NotNull E event) {
        validateNull(event);
        completed = event;

        if (thenExecuteConsumer != null) {
            thenExecuteConsumer.accept(event);
        }
    }
}