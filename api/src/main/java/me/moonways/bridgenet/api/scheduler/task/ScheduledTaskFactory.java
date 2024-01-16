package me.moonways.bridgenet.api.scheduler.task;

/**
 * Фабрика для создания новых
 * запланированных задач.
 */
@FunctionalInterface
public interface ScheduledTaskFactory {

    ScheduledTask createTaskInstance(long id);
}