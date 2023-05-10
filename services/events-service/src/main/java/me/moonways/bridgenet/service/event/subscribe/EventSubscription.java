package me.moonways.bridgenet.service.event.subscribe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.service.event.Event;
import me.moonways.bridgenet.service.event.EventFollower;
import me.moonways.bridgenet.service.event.EventService;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class EventSubscription<E extends Event> {

    @Getter
    private final Class<E> eventType;

    private final EventSubscribeExpiration expiration;
    private final EventSubscribePredication<E> predication;

    @Getter
    private final EventFollower<E> follower;

    public void followExpiration(@NotNull EventService eventService) {
        if (expiration == null || expiration.isTimeoutExpired())
            return;

        follower.follow(event -> {

            if (expiration.isTimeoutExpired()) {
                eventService.unsubscribe(this);
            }
        });
    }

    // todo
    public boolean predicate(@NotNull E event) {
        return predication.getPredicate().test(event);
    }
}
