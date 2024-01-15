package me.moonways.bridgenet.bootstrap.hook.jdbc;

import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import net.conveno.jdbc.ConvenoRouter;
import org.jetbrains.annotations.NotNull;

public class ConvenoJdbcHook extends BootstrapHook {

    @Inject
    private DependencyInjection dependencyInjection;

    @Override
    public void prepareExecution() {
        System.setProperty("system.jdbc.username", "username");
        System.setProperty("system.jdbc.password", "password");
    }

    @Override
    protected void postExecute(@NotNull AppBootstrap bootstrap) {
        ConvenoRouter convenoRouter = ConvenoRouter.create();
        dependencyInjection.bind(convenoRouter);
    }
}
