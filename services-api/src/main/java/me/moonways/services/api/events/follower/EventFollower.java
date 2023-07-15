package me.moonways.services.api.events.follower;

import me.moonways.services.api.events.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface EventFollower<E extends Event> {

    void postComplete(@NotNull E event);

    void follow(@Nullable Consumer<E> eventConsumer);

    boolean isCompleted();
}
