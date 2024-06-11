package me.moonways.bridgenet.test.engine.component.module.impl;

import me.moonways.bridgenet.rmi.service.RemoteServicesManagement;
import me.moonways.bridgenet.test.engine.component.module.ModuleAdapter;
import me.moonways.bridgenet.test.engine.component.module.ModuleConfig;

import java.util.Arrays;

public class RmiServicesModule extends ModuleAdapter {

    private static final String SERVICES_MODEL_PACKAGE = "me.moonways.bridgenet.model";

    public RmiServicesModule() {
        super(ModuleConfig.builder()
                .dependencies(
                        Arrays.asList(
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
