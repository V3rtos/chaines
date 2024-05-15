package me.moonways.bridgenet.mtp.message.response;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.autorun.persistence.AutoRunner;
import me.moonways.bridgenet.api.util.autorun.persistence.Runnable;
import me.moonways.bridgenet.api.util.autorun.persistence.RunningPeriod;

import java.util.concurrent.TimeUnit;

@AutoRunner
@RunningPeriod(period = 1, unit = TimeUnit.SECONDS)
public class ResponsibleMessageCleanUpRunner {

    @Inject
    private ResponsibleMessageService service;

    @Runnable
    public void run() {
        if (service != null) {
            service.cleanUp();
        }
    }
}
