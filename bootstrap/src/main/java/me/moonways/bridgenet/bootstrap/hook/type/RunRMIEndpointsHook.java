package me.moonways.bridgenet.bootstrap.hook.type;

import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.rsi.service.RemoteServicesManagement;
import org.jetbrains.annotations.NotNull;

public class RunRMIEndpointsHook extends BootstrapHook {

    @Inject
    private RemoteServicesManagement remoteServicesManagement;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        if (remoteServicesManagement != null) {

            remoteServicesManagement.initConfig();
            remoteServicesManagement.initEndpointsController();

            remoteServicesManagement.bindEndpoints();
        }
    }
}
