package me.moonways.bridgenet.scheduler.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.scheduler.ScheduleException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class TaskProcessFollower {

    private final ScheduledTask scheduledTask;
    private TaskProcess taskProcess;

    @Getter
    private long processedLoopsCount;

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
        validateNull(taskProcess);

        processedLoopsCount++;
        taskProcess.doProcess(scheduledTask);
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
