package me.moonways.bridgenet.scheduler.task;

import me.moonways.bridgenet.scheduler.ScheduleException;
import me.moonways.bridgenet.scheduler.ScheduledTime;
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
