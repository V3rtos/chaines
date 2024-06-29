package me.moonways.bridgenet.bootstrap.hook.type;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.rest4j.server.Bridgenet4jRestServer;
import org.jetbrains.annotations.NotNull;

public class RestServerStartHook extends BootstrapHook {
    @Inject
    private BeansService beansService;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        Bridgenet4jRestServer bridgenet4jRestServer = new Bridgenet4jRestServer();
        beansService.bind(bridgenet4jRestServer);

        bridgenet4jRestServer.start();
    }
}
