package me.moonways.bridgenet.bootstrap.hook.type;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.rest4j.server.RestBridgenetServer;
import org.jetbrains.annotations.NotNull;

public class RestServerStartHook extends BootstrapHook {

    @Inject
    private RestBridgenetServer restServer;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        if (restServer != null) {
            restServer.start();
        }
    }
}
