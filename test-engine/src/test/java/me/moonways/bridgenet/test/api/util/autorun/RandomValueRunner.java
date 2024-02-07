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
    public void regenerate() {
        props.setProperty(KEY, UUID.randomUUID().toString());
    }
}
