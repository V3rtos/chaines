package me.moonways.bridgenet.test.api.util.autorun;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.autorun.persistance.AutoRunner;
import me.moonways.bridgenet.api.util.autorun.persistance.Runnable;
import me.moonways.bridgenet.api.util.autorun.persistance.RunningPeriod;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@AutoRunner
@RunningPeriod(period = 1, unit = TimeUnit.SECONDS)
public class RandomValueRunner {

    public static final String KEY = "RandomValueRunner.value";

    @Inject
    private Properties props;

    @Runnable
    public void run_updateValue() {
        props.setProperty(KEY, generateStringValue());
    }

    private String generateStringValue() {
        return UUID.randomUUID().toString();
    }
}
