package me.moonways.bridgenet.bootstrap.hook.type.autorun;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.autorun.persistence.AutoRunner;
import me.moonways.bridgenet.api.autorun.persistence.Runnable;
import me.moonways.bridgenet.api.autorun.persistence.RunningPeriod;
import me.moonways.bridgenet.api.minecraft.ChatColor;
import me.moonways.bridgenet.api.util.pair.Pair;
import me.moonways.bridgenet.profiler.BridgenetDataLogger;

import java.util.concurrent.TimeUnit;

@Log4j2
@AutoRunner
@RunningPeriod(period = 1, unit = TimeUnit.MINUTES)
public class GarbageCollectorRunner {

    private long freeBefore = Runtime.getRuntime().freeMemory();

    @Inject
    private BridgenetDataLogger bridgenetDataLogger;

    @Runnable
    public void run() {
        Pair<Long, Long> freeMemoryPair = clearMemory();

        //log.debug("The memory used was automatically cleared by the Garbage Collector system: {}{}",
        //        getModifierColor(freeMemoryPair), freeMemoryPair);

        bridgenetDataLogger.logSystemMemoryFree();
        bridgenetDataLogger.logSystemMemoryTotal();
        bridgenetDataLogger.logSystemMemoryUsed();
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
