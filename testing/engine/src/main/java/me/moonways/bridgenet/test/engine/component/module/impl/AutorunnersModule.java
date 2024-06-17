package me.moonways.bridgenet.test.engine.component.module.impl;

import me.moonways.bridgenet.api.autorun.ScheduledRunnersService;
import me.moonways.bridgenet.test.engine.component.module.ModuleAdapter;
import me.moonways.bridgenet.test.engine.component.module.ModuleConfig;

import java.util.Collections;

public class AutorunnersModule extends ModuleAdapter {

    public AutorunnersModule() {
        super(ModuleConfig.builder()
                .dependencies(Collections.singletonList(SchedulersModule.class))
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(ScheduledRunnersService.class)
                        ))
                .build());
    }
}
