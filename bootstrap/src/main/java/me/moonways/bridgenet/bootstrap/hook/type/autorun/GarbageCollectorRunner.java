package me.moonways.bridgenet.bootstrap.hook.type.autorun;

import me.moonways.bridgenet.api.util.autorun.persistence.AutoRunner;
import me.moonways.bridgenet.api.util.autorun.persistence.Runnable;
import me.moonways.bridgenet.api.util.autorun.persistence.RunningPeriod;

import java.util.concurrent.TimeUnit;

@AutoRunner
@RunningPeriod(period = 1, unit = TimeUnit.MINUTES)
public class GarbageCollectorRunner {

    @Runnable
    public void run() {
        Runtime.getRuntime().gc();
    }
}
