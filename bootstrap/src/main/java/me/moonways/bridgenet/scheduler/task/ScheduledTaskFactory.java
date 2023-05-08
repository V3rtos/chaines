package me.moonways.bridgenet.scheduler.task;

@FunctionalInterface
public interface ScheduledTaskFactory {

    ScheduledTask createTaskInstance(long id);
}
