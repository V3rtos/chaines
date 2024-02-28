package me.moonways.bridgenet.bootstrap.hook.type.autorun;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.util.autorun.persistence.AutoRunner;
import me.moonways.bridgenet.api.util.autorun.persistence.Runnable;
import me.moonways.bridgenet.api.util.autorun.persistence.RunningPeriod;

import java.util.concurrent.TimeUnit;

@Log4j2
@AutoRunner
@RunningPeriod(period = 1, unit = TimeUnit.MINUTES)
public class GarbageCollectorRunner {

    @Runnable
    public void run() {
        Runtime.getRuntime().gc();
        log.debug("Memory was cleaned automatically");
    }
}
