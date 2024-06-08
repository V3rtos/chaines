package me.moonways.bridgenet.test.engine.module.impl;

import me.moonways.bridgenet.rest.server.WrappedHttpServer;
import me.moonways.bridgenet.test.engine.module.TestEngineModuleAdapter;
import me.moonways.bridgenet.test.engine.module.TestModuleBeans;

import java.util.Collections;

public class RestModule extends TestEngineModuleAdapter {

    public RestModule() {
        super(TestModuleBeans.builder()
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(WrappedHttpServer.class)
                        ))
                .build());
    }
}
