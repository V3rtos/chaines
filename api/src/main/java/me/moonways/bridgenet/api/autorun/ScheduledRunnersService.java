package me.moonways.bridgenet.api.autorun;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.autorun.persistence.AutoRunner;
import me.moonways.bridgenet.api.autorun.persistence.DelayedPeriod;
import me.moonways.bridgenet.api.autorun.persistence.RunUnit;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.processor.ScanningResult;
import me.moonways.bridgenet.api.inject.processor.persistence.AwaitAnnotationsScanning;
import me.moonways.bridgenet.api.inject.processor.persistence.GetAnnotationsScanningResult;
import me.moonways.bridgenet.api.scheduler.ScheduledTime;
import me.moonways.bridgenet.api.scheduler.Scheduler;
import me.moonways.bridgenet.api.util.reflection.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Autobind
@AwaitAnnotationsScanning(AutoRunner.class)
public class ScheduledRunnersService {

    private static final ScheduledTime DEFAULT_PERIOD = ScheduledTime.ofMillis(100);

    @Inject
    private Scheduler scheduler;

    @GetAnnotationsScanningResult
    private ScanningResult<Object> runnersResult;

    @PostConstruct
    private void init() {
        startGlobalTimer(runnersResult.toList());
    }

    private void startGlobalTimer(List<Object> runners) {
        List<RunnableUnit> units = toUnits(runners);
        Map<RunnableUnit, Long> unitsMap = units.stream()
                .collect(Collectors.toMap(
                        unit -> unit,
                        unit -> 0L));

        scheduler.schedule(ScheduledTime.ofSeconds(1), DEFAULT_PERIOD)
                .follow(new ScheduledRunnersInvocationTask(unitsMap));
    }

    private static List<RunnableUnit> toUnits(List<Object> runners) {
        List<RunnableUnit> result = new ArrayList<>();

        for (Object runner : runners) {
            Class<?> runnerClass = runner.getClass();
            DelayedPeriod periodAnnotation = runnerClass.getDeclaredAnnotation(DelayedPeriod.class);

            for (Method method : runnerClass.getDeclaredMethods()) {

                if (method.isAnnotationPresent(RunUnit.class)) {
                    DelayedPeriod periodUnitAnnotation = method.getDeclaredAnnotation(DelayedPeriod.class);
                    ScheduledTime period = DEFAULT_PERIOD;

                    if (periodUnitAnnotation != null) {
                        period = ScheduledTime.of(periodUnitAnnotation.period(), periodUnitAnnotation.unit());
                    } else if (periodAnnotation != null) {
                        period = ScheduledTime.of(periodAnnotation.period(), periodAnnotation.unit());
                    }

                    RunnableUnit runnableUnit = new RunnableUnit(period,
                            () -> ReflectionUtils.invoke(runner, method.getName()));

                    log.debug("Registering auto-runner §a'{}' §rwith id: §3{}", runnerClass.getSimpleName(), runnableUnit.getUuid());
                    result.add(runnableUnit);
                }
            }
        }

        return result;
    }
}
