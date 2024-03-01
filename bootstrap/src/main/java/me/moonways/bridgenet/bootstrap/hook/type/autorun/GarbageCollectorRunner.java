package me.moonways.bridgenet.bootstrap.hook.type.autorun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.autorun.persistence.AutoRunner;
import me.moonways.bridgenet.api.util.autorun.persistence.Runnable;
import me.moonways.bridgenet.api.util.autorun.persistence.RunningPeriod;
import me.moonways.bridgenet.api.util.minecraft.ChatColor;
import me.moonways.bridgenet.metrics.BridgenetMetricsLogger;

import java.util.concurrent.TimeUnit;

@Log4j2
@AutoRunner
@RunningPeriod(period = 1, unit = TimeUnit.MINUTES)
public class GarbageCollectorRunner {

    @Inject
    private BridgenetMetricsLogger bridgenetMetricsLogger;

    @Runnable
    public void run() {
        FreeMemoryPair freeMemoryPair = clearMemory();

        log.debug("The memory used was automatically cleared by the Garbage Collector system {}{}",
                getModifierColor(freeMemoryPair), freeMemoryPair);

        bridgenetMetricsLogger.logSystemMemoryFree();
        bridgenetMetricsLogger.logSystemMemoryTotal();
        bridgenetMetricsLogger.logSystemMemoryUsed();
    }

    private FreeMemoryPair clearMemory() {
        Runtime runtime = Runtime.getRuntime();

        long freeBefore = runtime.freeMemory();

        runtime.gc();
        long freeAfter = runtime.freeMemory();
        return new FreeMemoryPair(freeBefore, freeAfter);
    }

    private ChatColor getModifierColor(FreeMemoryPair pair) {
        long after = pair.getAfter();
        long before = pair.getBefore();

        return before > after ? ChatColor.GREEN : (before == after ? ChatColor.YELLOW : ChatColor.RED);
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    private static class FreeMemoryPair {
        private final long before;
        private final long after;
    }
}
