package me.moonways.bridgenet.test.engine.module.impl;

import me.moonways.bridgenet.rsi.service.RemoteServicesManagement;
import me.moonways.bridgenet.test.engine.module.TestEngineModuleAdapter;
import me.moonways.bridgenet.test.engine.module.TestModuleBeans;

import java.util.Arrays;
import java.util.Collections;

public class RmiServicesModule extends TestEngineModuleAdapter {
    private static final String SERVICES_MODEL_PACKAGE = "me.moonways.bridgenet.model";

    public RmiServicesModule() {
        super(TestModuleBeans.builder()
                .dependencies(
                        Arrays.asList(
                                SchedulersModule.class,
                                EventsModule.class,
                                CommandsModule.class,
                                DatabasesModule.class,
                                MtpModule.class
                        ))
                .packagesToScanning(
                        Arrays.asList(
                                SERVICES_MODEL_PACKAGE,
                                fromClassPackage(RemoteServicesManagement.class)
                        ))
                .build());
    }
}
