package me.moonways.bridgenet.test.engine.component.module.impl;

import me.moonways.bridgenet.api.scheduler.Scheduler;
import me.moonways.bridgenet.test.engine.component.module.ModuleAdapter;
import me.moonways.bridgenet.test.engine.component.module.ModuleConfig;

import java.util.Collections;

public class SchedulersModule extends ModuleAdapter {

    public SchedulersModule() {
        super(ModuleConfig.builder()
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(Scheduler.class)
                        ))
                .build());
    }
}
