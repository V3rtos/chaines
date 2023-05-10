package me.moonways.bridgenet.api.scheduler.task;

import me.moonways.bridgenet.api.scheduler.ScheduledTime;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ScheduledTask {

    long getId();

    @NotNull
    ScheduledTime getDelay();

    @Nullable
    ScheduledTime getPeriod();

    boolean isAsynchronous();

    boolean isCancelled();

    void shutdown();

    void forceShutdown();
}
