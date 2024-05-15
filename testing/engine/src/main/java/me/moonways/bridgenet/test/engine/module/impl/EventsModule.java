package me.moonways.bridgenet.test.engine.module.impl;

import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.test.engine.module.TestEngineModuleAdapter;
import me.moonways.bridgenet.test.engine.module.TestModuleBeans;

import java.util.Collections;

public class EventsModule extends TestEngineModuleAdapter {

    public EventsModule() {
        super(TestModuleBeans.builder()
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(Event.class)
                        ))
                .build());
    }
}
