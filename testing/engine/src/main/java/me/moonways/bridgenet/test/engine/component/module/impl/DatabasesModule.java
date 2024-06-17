package me.moonways.bridgenet.test.engine.component.module.impl;

import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.test.engine.component.module.ModuleAdapter;
import me.moonways.bridgenet.test.engine.component.module.ModuleConfig;

import java.util.Collections;

public class DatabasesModule extends ModuleAdapter {

    public DatabasesModule() {
        super(ModuleConfig.builder()
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(DatabaseProvider.class)
                        ))
                .build());
    }
}
