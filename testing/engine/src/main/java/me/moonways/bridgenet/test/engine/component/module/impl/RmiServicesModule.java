package me.moonways.bridgenet.test.engine.component.module.impl;

import me.moonways.bridgenet.rmi.service.RemoteServicesManagement;
import me.moonways.bridgenet.test.engine.component.module.ModuleAdapter;
import me.moonways.bridgenet.test.engine.component.module.ModuleConfig;

import java.util.Arrays;

public class RmiServicesModule extends ModuleAdapter {

    private static final String SERVICES_MODEL__PACKAGE = "me.moonways.bridgenet.model";

    public RmiServicesModule() {
        super(ModuleConfig.builder()
                .dependencies(
                        Arrays.asList(
                                EventsModule.class,
                                DatabasesModule.class,
                                MtpModule.class,
                                CommandsModule.class
                        ))
                .packagesToScanning(
                        Arrays.asList(
                                SERVICES_MODEL__PACKAGE,
                                fromClassPackage(RemoteServicesManagement.class)
                        ))
                .build());
    }
}
