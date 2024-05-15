package me.moonways.bridgenet.test.engine.module.impl;

import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.api.scheduler.Scheduler;
import me.moonways.bridgenet.test.engine.module.TestEngineModuleAdapter;
import me.moonways.bridgenet.test.engine.module.TestModuleBeans;

import java.util.Collections;

public class SchedulersModule extends TestEngineModuleAdapter {

    public SchedulersModule() {
        super(TestModuleBeans.builder()
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(Scheduler.class)
                        ))
                .build());
    }
}
