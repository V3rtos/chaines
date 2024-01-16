package me.moonways.bridgenet.api.event.subscribe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.api.event.EventFollower;
import me.moonways.bridgenet.api.event.EventService;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class EventSubscription<E extends Event> {

    @Getter
    private final Class<E> eventType;

    private final EventSubscribeExpiration expiration;
    private final EventSubscribePredication<E> predication;

    @Getter
    private final EventFollower<E> follower;

    public void followExpiration(@NotNull EventService eventManager) {
        if (expiration == null || expiration.isTimeoutExpired())
            return;

        follower.follow(event -> {

            if (expiration.isTimeoutExpired()) {
                eventManager.unsubscribe(this);
            }
        });
    }

    public boolean predicate(@NotNull E event) {
        return predication.getPredicate().test(event);
    }
}
