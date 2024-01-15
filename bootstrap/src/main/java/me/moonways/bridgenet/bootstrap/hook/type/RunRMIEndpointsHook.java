package me.moonways.bridgenet.bootstrap.hook.type;

import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.ApplicationBootstrapHook;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.rsi.service.RemoteServiceRegistry;
import org.jetbrains.annotations.NotNull;

public class RunRMIEndpointsHook extends ApplicationBootstrapHook {

    @Inject
    private RemoteServiceRegistry remoteServiceRegistry;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        remoteServiceRegistry.initializeXmlConfiguration();
        remoteServiceRegistry.initializeEndpointsController();
    }
}
