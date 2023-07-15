package me.moonways.service.event.handle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.services.api.events.Event;
import me.moonways.services.api.events.EventException;
import me.moonways.services.api.events.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.util.Objects;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EventInvoker<E extends Event> {

    private final Object handler;

    @Getter
    private final EventPriority priority;

    private final Class<E> eventType;
    private final MethodHandle methodHandle;

    private void validateNull(Event event) {
        if (event == null) {
            throw new EventException("event is null");
        }
    }

    private void validateType(Event event) {
        if (event.getClass() != eventType) {
            throw new EventException("handled invoker event type is not native");
        }
    }

    public void invoke(@NotNull Event event) {
        try {
            validateNull(event);
            validateType(event);

            methodHandle.invoke(handler, event);
        }
        catch (Throwable exception) {
            throw new EventException(exception, "Internal event handle error - {0}", event.getClass());
        }
    }

    public boolean isTyped(@NotNull Class<? extends Event> otherEventType) {
        return Objects.equals(eventType, otherEventType) || eventType.isAssignableFrom(otherEventType);
    }
}
