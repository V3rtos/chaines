package me.moonways.bridgenet.bootstrap.hook.rsi;

import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.rsi.service.RemoteServiceRegistry;
import org.jetbrains.annotations.NotNull;

public class ServiceInitHook extends BootstrapHook {

    @Inject
    private RemoteServiceRegistry remoteServiceRegistry;

    @Override
    protected void postExecute(@NotNull AppBootstrap bootstrap) {
        remoteServiceRegistry.initializeXmlConfiguration();
        remoteServiceRegistry.initializeEndpointsController();
    }
}
