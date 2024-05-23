package me.moonways.bridgenet.api.scheduler;

import me.moonways.bridgenet.api.scheduler.task.ScheduledTask;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ScheduledQueue {

    private final List<ScheduledTask> scheduledTasks = Collections.synchronizedList(new LinkedList<>());
    private long lastIndexedId;

    private void validateNull(ScheduledTask scheduledTask) {
        if (scheduledTask == null) {
            throw new ScheduleException("scheduled task is null");
        }
    }

    public synchronized void push(ScheduledTask scheduledTask) {
        validateNull(scheduledTask);
        scheduledTasks.add(scheduledTask);
    }

    public synchronized ScheduledTask poll(long id) {
        ScheduledTask peek = peek(id);
        scheduledTasks.remove(peek);

        return peek;
    }

    public synchronized ScheduledTask peek(long id) {
        return peekNullable(id)
                .orElseThrow(() -> new ScheduleException("scheduled task by id {0} is not found", id));
    }

    public synchronized Optional<ScheduledTask> peekNullable(long id) {
        return scheduledTasks
                .stream()
                .filter(scheduledTask -> scheduledTask.getId() == id)
                .findFirst();
    }

    public synchronized long nextId() {
        lastIndexedId++;
        if (lastIndexedId == Long.MAX_VALUE)
            lastIndexedId = 0;

        return lastIndexedId;
    }
}
