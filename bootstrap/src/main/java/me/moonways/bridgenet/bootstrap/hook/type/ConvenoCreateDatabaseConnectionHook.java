package me.moonways.bridgenet.bootstrap.hook.type;

import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.ApplicationBootstrapHook;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import net.conveno.jdbc.ConvenoRouter;
import org.jetbrains.annotations.NotNull;

public class ConvenoCreateDatabaseConnectionHook extends ApplicationBootstrapHook {

    @Inject
    private DependencyInjection injector;

    @Override
    public void onBefore() {
        System.setProperty("system.jdbc.username", "username");
        System.setProperty("system.jdbc.password", "password");
    }

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        ConvenoRouter convenoRouter = ConvenoRouter.create();
        injector.bind(convenoRouter);
    }
}
