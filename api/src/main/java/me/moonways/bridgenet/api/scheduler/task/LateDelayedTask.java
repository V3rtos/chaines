package me.moonways.bridgenet.api.scheduler.task;

import me.moonways.bridgenet.api.scheduler.ScheduleException;
import me.moonways.bridgenet.api.scheduler.ScheduledTime;
import org.jetbrains.annotations.NotNull;

public class LateDelayedTask extends AbstractDelayedTask {

    public LateDelayedTask(long id, @NotNull ScheduledTime delay) {
        super(id, delay, null);
        validateNull(delay);
    }

    private void validateNull(ScheduledTime scheduledTime) {
        if (scheduledTime == null) {
            throw new ScheduleException("scheduled time is null");
        }
    }
}
