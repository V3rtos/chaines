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
import me.moonways.bridgenet.api.util.pair.Pair;
import me.moonways.bridgenet.metrics.BridgenetMetricsLogger;

import java.util.concurrent.TimeUnit;

@Log4j2
@AutoRunner
@RunningPeriod(period = 1, unit = TimeUnit.MINUTES)
public class GarbageCollectorRunner {

    private long freeBefore = Runtime.getRuntime().freeMemory();

    @Inject
    private BridgenetMetricsLogger bridgenetMetricsLogger;

    @Runnable
    public void run() {
        Pair<Long, Long> freeMemoryPair = clearMemory();

        //log.debug("The memory used was automatically cleared by the Garbage Collector system: {}{}",
        //        getModifierColor(freeMemoryPair), freeMemoryPair);

        bridgenetMetricsLogger.logSystemMemoryFree();
        bridgenetMetricsLogger.logSystemMemoryTotal();
        bridgenetMetricsLogger.logSystemMemoryUsed();
    }

    private Pair<Long, Long> clearMemory() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();

        long freeAfter = runtime.freeMemory();

        Pair<Long, Long> pair = Pair.immutable(freeBefore, freeAfter);
        freeBefore = freeAfter;

        return pair;
    }

    private ChatColor getModifierColor(Pair<Long, Long> pair) {
        long after = pair.first();
        long before = pair.second();

        return before > after ? ChatColor.GREEN : (before == after ? ChatColor.YELLOW : ChatColor.RED);
    }
}
