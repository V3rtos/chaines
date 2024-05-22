package me.moonways.bridgenet.api.scheduler.task;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.scheduler.ScheduleException;
import me.moonways.bridgenet.api.scheduler.ScheduledTime;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Future;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class AbstractDelayedTask implements ScheduledTask {

    private final Object lock = new Object();

    @ToString.Include
    private final long id;

    @ToString.Include
    private final ScheduledTime delay, period;

    private boolean isAsynchronous;
    @ToString.Include
    private boolean cancelled;

    private Future<?> future;

    private void validateAlreadyCancelled() {
        if (isCancelled()) {
            throw new ScheduleException("Task {0} is already cancelled", id);
        }
    }

    protected void markAsynchronous() {
        synchronized (lock) {
            isAsynchronous = Boolean.TRUE;
        }
    }

    private void shutdown0(boolean force) {
        validateAlreadyCancelled();

        synchronized (lock) {
            cancelled = true;

            if (future != null) {
                future.cancel(force);
            }
        }
    }

    public void setFuture(@Nullable Future<?> future) {
        if (future != null) {
            markAsynchronous();
        }

        this.future = future;
    }

    @Override
    public void shutdown() {
        shutdown0(false);
    }

    @Override
    public void forceShutdown() {
        shutdown0(true);
    }
}
