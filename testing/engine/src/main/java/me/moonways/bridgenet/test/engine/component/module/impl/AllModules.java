package me.moonways.bridgenet.test.engine.component.module.impl;

import me.moonways.bridgenet.test.engine.component.module.ModuleAdapter;
import me.moonways.bridgenet.test.engine.component.module.ModuleConfig;

import java.util.Collections;

public final class AllModules extends ModuleAdapter {
    public AllModules() {
        super(ModuleConfig.builder()
                .packagesToScanning(Collections.singletonList(""))
                .build());
    }
}
