package me.moonways.bridgenet.test.engine.component.module.impl;

import me.moonways.bridgenet.rest4j.server.RestBridgenetServer;
import me.moonways.bridgenet.test.engine.component.module.ModuleAdapter;
import me.moonways.bridgenet.test.engine.component.module.ModuleConfig;

import java.util.Collections;

public class RestModule extends ModuleAdapter {

    public RestModule() {
        super(ModuleConfig.builder()
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(RestBridgenetServer.class)
                        ))
                .build());
    }
}
