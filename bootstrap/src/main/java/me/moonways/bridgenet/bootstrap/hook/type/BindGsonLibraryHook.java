package me.moonways.bridgenet.bootstrap.hook.type;

import com.google.gson.Gson;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.ApplicationBootstrapHook;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import org.jetbrains.annotations.NotNull;

public class BindGsonLibraryHook extends ApplicationBootstrapHook {

    @Inject
    private DependencyInjection dependencyInjection;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        dependencyInjection.bind(new Gson());
    }
}
