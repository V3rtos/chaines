package me.moonways.bridgenet.test.engine.module.impl;

import me.moonways.bridgenet.api.autorun.ScheduledRunnersService;
import me.moonways.bridgenet.test.engine.module.TestEngineModuleAdapter;
import me.moonways.bridgenet.test.engine.module.TestModuleBeans;

import java.util.Collections;

public class AutorunnersModule extends TestEngineModuleAdapter {

    public AutorunnersModule() {
        super(TestModuleBeans.builder()
                .dependencies(Collections.singletonList(SchedulersModule.class))
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(ScheduledRunnersService.class)
                        ))
                .build());
    }
}
