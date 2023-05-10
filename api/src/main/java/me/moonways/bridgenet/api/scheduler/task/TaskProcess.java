package me.moonways.bridgenet.api.scheduler.task;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TaskProcess {

    void doProcess(@NotNull ScheduledTask scheduledTask);

    default TaskProcess andFollow(@NotNull TaskProcess taskProcess) {
        return (scheduledTask -> {doProcess(scheduledTask); taskProcess.doProcess(scheduledTask);});
    }
}
