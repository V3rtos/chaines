package me.moonways.bridgenet.test.engine.component.module.impl;

import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.test.engine.component.module.ModuleAdapter;
import me.moonways.bridgenet.test.engine.component.module.ModuleConfig;

import java.util.Collections;

public class EventsModule extends ModuleAdapter {

    public EventsModule() {
        super(ModuleConfig.builder()
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(Event.class)
                        ))
                .build());
    }
}
