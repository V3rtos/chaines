package me.moonways.bridgenet.mtp.message.response;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.autorun.persistence.AutoRunner;
import me.moonways.bridgenet.api.autorun.persistence.RunUnit;
import me.moonways.bridgenet.api.autorun.persistence.DelayedPeriod;

import java.util.concurrent.TimeUnit;

@AutoRunner
@DelayedPeriod(period = 1, unit = TimeUnit.SECONDS)
public class ResponsibleMessageCleanUpRunner {

    @Inject
    private ResponsibleMessageService service;

    @RunUnit
    public void run() {
        if (service != null) {
            service.cleanUp();
        }
    }
}
