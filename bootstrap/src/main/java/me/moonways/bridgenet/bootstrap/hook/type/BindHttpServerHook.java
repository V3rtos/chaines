package me.moonways.bridgenet.bootstrap.hook.type;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.rest.server.WrappedHttpServer;
import org.jetbrains.annotations.NotNull;

public class BindHttpServerHook extends BootstrapHook {

    @Inject
    private WrappedHttpServer httpServer;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        if (httpServer != null) {
            httpServer.bind();
        }
    }
}
