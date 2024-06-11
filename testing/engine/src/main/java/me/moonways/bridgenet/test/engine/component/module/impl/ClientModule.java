package me.moonways.bridgenet.test.engine.component.module.impl;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.rmi.service.RemoteServicesManagement;
import me.moonways.bridgenet.test.data.ExampleClient;
import me.moonways.bridgenet.test.engine.component.module.ModuleAdapter;
import me.moonways.bridgenet.test.engine.component.module.ModuleConfig;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ClientModule extends ModuleAdapter {
    private static final ExecutorService clientExecutor = Threads.newSingleThreadExecutor();

    @Inject
    private RemoteServicesManagement remoteServicesManagement;
    @Inject
    private BeansService beansService;

    public ClientModule() {
        super(ModuleConfig.builder()
                .dependencies(
                        Collections.singletonList(
                                RmiServicesModule.class
                        ))
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(RemoteServicesManagement.class)
                        ))
                .build());
    }

    @Override
    public void install(TestFlowContext context) {
        remoteServicesManagement.subscribeExportedAll(this::bindClient);
    }

    private void bindClient() {
        ExampleClient exampleClient = new ExampleClient();
        exampleClient.start();

        beansService.justStore(exampleClient);
    }
}
