package me.moonways.bridgenet.test.data.management;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.autorun.persistence.AutoRunner;
import me.moonways.bridgenet.api.autorun.persistence.DelayedPeriod;
import me.moonways.bridgenet.api.autorun.persistence.RunUnit;
import me.moonways.bridgenet.api.inject.Inject;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Log4j2
@AutoRunner
@DelayedPeriod(period = 500, unit = TimeUnit.MILLISECONDS)
public class ExampleSetPropertyRunner {

    public static final String KEY = "RandomValueRunner.value";

    @Inject
    private Properties props;

    @RunUnit
    public void regenerate() {
        String value = UUID.randomUUID().toString();

        if (props != null) {
            props.setProperty(KEY, value);
        }
    }
}
