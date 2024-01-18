package me.moonways.bridgenet.mtp.pipeline.response;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.autorun.persistance.AutoRunner;
import me.moonways.bridgenet.api.util.autorun.persistance.Runnable;
import me.moonways.bridgenet.api.util.autorun.persistance.RunningPeriod;

import java.util.concurrent.TimeUnit;

@AutoRunner
@RunningPeriod(period = 1, unit = TimeUnit.SECONDS)
public class DefaultMessageResponseRunner {

    @Inject
    private DefaultMessageResponseService service;

    @Runnable
    public void run() {
        service.cleanUp();
    }
}
