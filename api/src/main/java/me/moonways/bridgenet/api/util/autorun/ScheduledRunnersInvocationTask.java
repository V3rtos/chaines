package me.moonways.bridgenet.api.util.autorun;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.scheduler.ScheduledTime;
import me.moonways.bridgenet.api.scheduler.task.ScheduledTask;
import me.moonways.bridgenet.api.scheduler.task.TaskProcess;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class ScheduledRunnersInvocationTask implements TaskProcess {

    private final Map<RunnableUnit, Long> unitsWithLastRunningTimeMap;

    @Override
    public void doProcess(@NotNull ScheduledTask scheduledTask) {
        for (RunnableUnit unit : unitsWithLastRunningTimeMap.keySet()) {
            ScheduledTime period = unit.getPeriod();

            if (System.currentTimeMillis() - unitsWithLastRunningTimeMap.get(unit) >= period.toMillis()) {
                try {
                    unit.getRunnable().run();
                } catch (Throwable exception) {
                    log.error("§4Runner unit '{}' has received exception", unit.getUuid(), exception);
                } finally {
                    unitsWithLastRunningTimeMap.put(unit, System.currentTimeMillis());
                }
            }
        }
    }
}
