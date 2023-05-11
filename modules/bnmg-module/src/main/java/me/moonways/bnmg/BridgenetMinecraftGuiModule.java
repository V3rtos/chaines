package me.moonways.bnmg;

import me.moonways.bridgenet.service.inject.DependencyInjection;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.bridgenet.api.module.AbstractModule;
import me.moonways.bridgenet.api.module.ModuleIdentifier;
import me.moonways.bridgenet.service.bnmg.BnmgService;

@ModuleIdentifier(id = "bnmg", name = "BridgeNetMinecraftGui", version = "1.0")
public class BridgenetMinecraftGuiModule extends AbstractModule {

    private final BnmgService minecraftGuiService = new BnmgService();;

    @Inject
    private DependencyInjection dependencyInjection;

    @Override
    public void onEnable() {
        dependencyInjection.addDepend(minecraftGuiService);
    }
}
