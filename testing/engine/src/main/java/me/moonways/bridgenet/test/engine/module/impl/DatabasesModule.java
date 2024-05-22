package me.moonways.bridgenet.test.engine.module.impl;

import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.test.engine.module.TestEngineModuleAdapter;
import me.moonways.bridgenet.test.engine.module.TestModuleBeans;

import java.util.Collections;

public class DatabasesModule extends TestEngineModuleAdapter {

    public DatabasesModule() {
        super(TestModuleBeans.builder()
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(DatabaseProvider.class)
                        ))
                .build());
    }
}
