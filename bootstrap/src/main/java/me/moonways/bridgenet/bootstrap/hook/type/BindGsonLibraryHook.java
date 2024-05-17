package me.moonways.bridgenet.bootstrap.hook.type;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import org.jetbrains.annotations.NotNull;

public class BindGsonLibraryHook extends BootstrapHook {

    @Inject
    private BeansService beansService;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        beansService.bind(new GsonBuilder());
        beansService.bind(new GsonBuilder().setLenient().create()); // com.google.gson.Gson
    }
}
