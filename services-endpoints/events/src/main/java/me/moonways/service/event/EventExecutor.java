package me.moonways.service.event;

import lombok.RequiredArgsConstructor;
import me.moonways.service.api.events.Cancellable;
import me.moonways.service.api.events.EventPriority;
import me.moonways.service.api.events.AsyncEvent;
import me.moonways.service.api.events.Event;
import me.moonways.service.api.events.exception.EventException;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public final class EventExecutor {

    private final ExecutorService executorService;

    private final EventRegistry eventRegistry;

    private void validateNull(Event event) {
        if (event == null) {
            throw new EventException("event is null");
        }
    }

    private void validateNotCancellations(Event event) {
        if (event instanceof Cancellable) {
            throw new EventException("async event is not be assign Cancellable");
        }
    }

    @NotNull
    public <E extends Event> EventFutureImpl<E> fireEvent(E event) {
        validateNull(event);

        if (event instanceof AsyncEvent) {
            return supplyAsynchronous(event);
        }

        EventFutureImpl<E> eventFutureImpl = createFuture(event, false);
        fireEventNaturally(event, eventFutureImpl);

        return eventFutureImpl;
    }

    private <E extends Event> EventFutureImpl<E> supplyAsynchronous(E event) {
        validateNotCancellations(event);

        EventFutureImpl<E> eventFutureImpl = createFuture(event, true);
        executorService.submit(() -> fireEventNaturally(event, eventFutureImpl));

        return eventFutureImpl;
    }

    private boolean canCancellations(Event event) {
        boolean result = (event instanceof Cancellable);
        if (result) {
            try {
                ((Cancellable) event).makeNotCancelled();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    private  <E extends Event> void fireEventNaturally(E event, EventFutureImpl<E> eventFutureImpl) {
        eventRegistry.findInvokersByPriority(event.getClass())
                        .forEach(invoker -> invoker.invoke(event));

        eventFutureImpl.complete(event);
    }

    private <E extends Event> EventFutureImpl<E> createFuture(E event, boolean isAsync) {
        boolean isCancellable = canCancellations(event);

        return new EventFutureImpl<>(executorService, EventPriority.NORMAL, isAsync, isCancellable);
    }
}
