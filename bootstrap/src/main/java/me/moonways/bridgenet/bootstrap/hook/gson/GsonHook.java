package me.moonways.bridgenet.bootstrap.hook.gson;

import com.google.gson.Gson;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
import org.jetbrains.annotations.NotNull;

public class GsonHook extends BootstrapHook {

    @Inject
    private DependencyInjection dependencyInjection;

    @Override
    protected void postExecute(@NotNull AppBootstrap bootstrap) {
        dependencyInjection.bind(new Gson());
    }
}
