package me.moonways.bridgenet.api.scheduler.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.scheduler.ScheduleException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class TaskProcessFollower {

    private final ScheduledTask scheduledTask;
    private TaskProcess taskProcess;

    @Getter
    private long processedLoopsCount;

    private CompletableFuture<Object> completion;

    public void join() {
        completion = new CompletableFuture<>();
        completion.join();
    }

    private void validateNull(TaskProcess taskProcess) {
        if (taskProcess == null) {
            throw new ScheduleException("task process is null");
        }
    }

    private void validateNull(ScheduledTask scheduledTask) {
        if (scheduledTask == null) {
            throw new ScheduleException("scheduled task is null");
        }
    }

    private void validateCancelled(ScheduledTask scheduledTask) {
        if (scheduledTask.isCancelled()) {
            throw new ScheduleException("scheduled task is cancelled");
        }
    }

    public synchronized void post() {
        processedLoopsCount++;

        if (taskProcess != null) {
            taskProcess.doProcess(scheduledTask);
        }
        if (completion != null) {
            completion.complete(new Object());
        }
    }

    @NotNull
    public TaskProcessFollower follow(@NotNull TaskProcess follow) {
        validateNull(follow);

        validateNull(scheduledTask);
        validateCancelled(scheduledTask);

        if (taskProcess == null)
            taskProcess = follow;
        else
            taskProcess = taskProcess.andFollow(follow);

        return this;
    }
}
