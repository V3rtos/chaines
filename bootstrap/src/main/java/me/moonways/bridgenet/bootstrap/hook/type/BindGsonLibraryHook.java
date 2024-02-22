package me.moonways.bridgenet.bootstrap.hook.type;

import com.google.gson.Gson;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.ApplicationBootstrapHook;
import me.moonways.bridgenet.api.inject.Inject;
import org.jetbrains.annotations.NotNull;

public class BindGsonLibraryHook extends ApplicationBootstrapHook {

    @Inject
    private BeansService beansService;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        beansService.bind(new Gson());
    }
}
