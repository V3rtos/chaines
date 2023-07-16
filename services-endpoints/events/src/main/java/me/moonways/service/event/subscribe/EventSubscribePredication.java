package me.moonways.service.event.subscribe;

import lombok.*;
import me.moonways.service.api.events.Event;
import me.moonways.service.api.events.exception.EventException;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EventSubscribePredication<E extends Event> {

    private static final Predicate<Event> DEFAULT_PREDICATION = ((event) -> true);

    public static <E extends Event> EventSubscribePredication<E> create(Predicate<E> predicate) {
        return new EventSubscribePredication<>(predicate);
    }

    public static <E extends Event> EventSubscribePredication<E> create() {
        return new EventSubscribePredication<>();
    }

    @Getter
    private Predicate<E> predicate;

    private void validate(Predicate<E> predicate) {
        if (predicate == null) {
            throw new EventException("predication is null");
        }
    }

    @SuppressWarnings("unchecked")
    private Predicate<E> current() {
        if (predicate == null) {
            predicate = (Predicate<E>) DEFAULT_PREDICATION;
        }

        return predicate;
    }

    public EventSubscribePredication<E> makeInverse() {
        validate(predicate);
        predicate = predicate.negate();

        return this;
    }

    public EventSubscribePredication<E> and(@NotNull Predicate<E> predicate) {
        validate(predicate);
        this.predicate = current().and(predicate);

        return this;
    }

    public EventSubscribePredication<E> or(@NotNull Predicate<E> predicate) {
        validate(predicate);
        this.predicate = current().or(predicate);

        return this;
    }
}
