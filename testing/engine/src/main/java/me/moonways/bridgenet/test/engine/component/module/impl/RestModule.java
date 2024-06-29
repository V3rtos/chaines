package me.moonways.bridgenet.test.engine.component.module.impl;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.rest4j.server.Bridgenet4jRestServer;
import me.moonways.bridgenet.rest4j.server.accesstoken.AccessTokenSource;
import me.moonways.bridgenet.rest4j.server.accesstoken.Bridgenet4jAccessTokenService;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.component.module.ModuleAdapter;
import me.moonways.bridgenet.test.engine.component.module.ModuleConfig;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;

import java.util.Collections;

public class RestModule extends ModuleAdapter {

    @Inject
    private BeansService beansService;

    public RestModule() {
        super(ModuleConfig.builder()
                .packagesToScanning(
                        Collections.singletonList(
                                fromClassPackage(Bridgenet4jRestServer.class)
                        ))
                .build());
    }

    @Override
    public void install(TestFlowContext context) {
        beansService.subscribeOn(Bridgenet4jAccessTokenService.class,
                bridgenet4jAccessTokenService -> bridgenet4jAccessTokenService.addAccessToken(
                        AccessTokenSource.TESTING,
                        TestConst.Rest.API_ACCESS_KEY));
    }
}
