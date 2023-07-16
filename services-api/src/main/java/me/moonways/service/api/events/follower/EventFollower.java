package me.moonways.service.api.events.follower;

import me.moonways.service.api.events.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface EventFollower<E extends Event> {

    void postComplete(@NotNull E event);

    void follow(@Nullable Consumer<E> eventConsumer);

    boolean isCompleted();
}
