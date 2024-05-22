package me.moonways.bridgenet.test.engine.module.impl;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.module.TestEngineModuleAdapter;
import me.moonways.bridgenet.test.engine.module.TestModuleBeans;

import java.util.Collections;

public class DatabasesModule extends TestEngineModuleAdapter {

    @Inject
    private BeansService beansService;

    public DatabasesModule() {
        super(TestModuleBeans.builder()
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(DatabaseProvider.class)
                        ))
                .build());
    }

    @Override
    public void onInstall(TestFlowContext context) {
        beansService.onBinding(DatabaseConnection.class, (connection) -> connection.call("SET MODE MySQL;"));
    }
}
