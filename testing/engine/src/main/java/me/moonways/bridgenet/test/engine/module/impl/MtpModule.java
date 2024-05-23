package me.moonways.bridgenet.test.engine.module.impl;

import me.moonways.bridgenet.mtp.BridgenetNetworkController;
import me.moonways.bridgenet.test.engine.module.TestEngineModuleAdapter;
import me.moonways.bridgenet.test.engine.module.TestModuleBeans;

import java.util.Collections;

public class MtpModule extends TestEngineModuleAdapter {

    public MtpModule() {
        super(TestModuleBeans.builder()
                .dependencies(Collections.singletonList(RmiServicesModule.class))
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(BridgenetNetworkController.class)
                        ))
                .build());
    }
}
