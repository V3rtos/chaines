package me.moonways.bridgenet.api.scheduler.task;

@FunctionalInterface
public interface ScheduledTaskFactory {

    ScheduledTask createTaskInstance(long id);
}
