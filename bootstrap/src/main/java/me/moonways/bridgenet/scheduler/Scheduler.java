package me.moonways.bridgenet.scheduler;

import me.moonways.bridgenet.scheduler.task.ScheduledTaskExecutor;
import me.moonways.bridgenet.scheduler.task.TaskFuture;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class Scheduler {

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
    private final ScheduledTaskExecutor scheduledTaskExecutor = new ScheduledTaskExecutor(executorService);

    @NotNull
    public synchronized TaskFuture scheduleSynchronized(@NotNull ScheduledTime delay) {
        return scheduledTaskExecutor.scheduleSynchronized(delay);
    }

    @NotNull
    public synchronized TaskFuture scheduleSynchronized(@NotNull ScheduledTime delay, @NotNull ScheduledTime period) {
        return scheduledTaskExecutor.scheduleSynchronized(delay, period);
    }

    @NotNull
    public synchronized TaskFuture schedule(@NotNull ScheduledTime delay) {
        return scheduledTaskExecutor.schedule(delay);
    }

    @NotNull
    public synchronized TaskFuture schedule(@NotNull ScheduledTime delay, @NotNull ScheduledTime period) {
        return scheduledTaskExecutor.schedule(delay, period);
    }
}
