package me.moonways.bridgenet.test.engine.module.impl;

import me.moonways.bridgenet.test.engine.module.TestEngineModuleAdapter;
import me.moonways.bridgenet.test.engine.module.TestModuleBeans;

import java.util.Collections;

public final class AllModules extends TestEngineModuleAdapter {
    public AllModules() {
        super(TestModuleBeans.builder()
                .packagesToScanning(Collections.singletonList(""))
                .build());
    }
}
