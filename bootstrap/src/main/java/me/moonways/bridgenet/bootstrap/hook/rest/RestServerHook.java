package me.moonways.bridgenet.bootstrap.hook.rest;

import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.rest.server.WrappedHttpServer;
import org.jetbrains.annotations.NotNull;

public class RestServerHook extends BootstrapHook {

    @Inject
    private WrappedHttpServer httpServer;

    @Override
    protected void postExecute(@NotNull AppBootstrap bootstrap) {
        httpServer.bindSync();
    }
}
