package me.moonways.bridgenet.bootstrap.autorun;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.autorun.persistence.AutoRunner;
import me.moonways.bridgenet.api.autorun.persistence.RunUnit;
import me.moonways.bridgenet.api.autorun.persistence.DelayedPeriod;
import me.moonways.bridgenet.profiler.BridgenetDataLogger;

import java.util.concurrent.TimeUnit;

@Log4j2
@AutoRunner
@DelayedPeriod(period = 1, unit = TimeUnit.MINUTES)
public class MemoryDataLoggingAutorunner {

    @Inject
    private BridgenetDataLogger bridgenetDataLogger;

    @RunUnit
    public void run() {
        System.gc();

        if (bridgenetDataLogger != null) {

            bridgenetDataLogger.logRuntimeMemoryFree();
            bridgenetDataLogger.logRuntimeMemoryTotal();
            bridgenetDataLogger.logRuntimeMemoryUsed();
        }
    }
}
