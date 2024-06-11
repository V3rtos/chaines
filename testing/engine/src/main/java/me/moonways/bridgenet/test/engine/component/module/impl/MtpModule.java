package me.moonways.bridgenet.test.engine.component.module.impl;

import me.moonways.bridgenet.mtp.BridgenetNetworkController;
import me.moonways.bridgenet.test.engine.component.module.ModuleAdapter;
import me.moonways.bridgenet.test.engine.component.module.ModuleConfig;

import java.util.Collections;

public class MtpModule extends ModuleAdapter {

    public MtpModule() {
        super(ModuleConfig.builder()
                .dependencies(Collections.singletonList(RmiServicesModule.class))
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(BridgenetNetworkController.class)
                        ))
                .build());
    }
}
