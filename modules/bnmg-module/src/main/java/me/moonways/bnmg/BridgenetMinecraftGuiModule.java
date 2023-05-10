package me.moonways.bnmg;

import me.moonways.bridgenet.service.inject.DependencyInjection;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.bridgenet.api.module.AbstractModule;
import me.moonways.bridgenet.api.module.ModuleIdentifier;
import me.moonways.bridgenet.service.bnmg.BridgenetMinecraftGuiService;

@ModuleIdentifier(id = "2", name = "BNMG", version = "1.0")
public class BridgenetMinecraftGuiModule extends AbstractModule {

    private final BridgenetMinecraftGuiService minecraftGuiService = new BridgenetMinecraftGuiService();;

    @Inject
    private DependencyInjection dependencyInjection;

    @Override
    public void onEnable() {
        dependencyInjection.addDepend(minecraftGuiService);
    }
}
