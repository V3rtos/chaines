package me.moonways.bridgenet.api.scheduler.task;

import me.moonways.bridgenet.api.scheduler.ScheduleException;
import me.moonways.bridgenet.api.scheduler.ScheduledTime;
import org.jetbrains.annotations.NotNull;

public class InfinityDelayedTask extends AbstractDelayedTask {

    public InfinityDelayedTask(long id, @NotNull ScheduledTime delay, @NotNull ScheduledTime period) {
        super(id, delay, period);

        validateNull(delay);
        validateNull(period);
    }

    private void validateNull(ScheduledTime scheduledTime) {
        if (scheduledTime == null) {
            throw new ScheduleException("scheduled time is null");
        }
    }
}
