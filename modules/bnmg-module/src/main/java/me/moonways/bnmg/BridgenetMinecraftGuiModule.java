package me.moonways.bnmg;

import me.moonways.bridgenet.api.BridgenetControl;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.module.AbstractModule;
import me.moonways.bridgenet.api.module.ModuleIdentifier;

@ModuleIdentifier(id = "2", name = "BNMG", version = "1.0")
public class BridgenetMinecraftGuiModule extends AbstractModule {

    private final BridgenetMinecraftGuiService minecraftGuiService = new BridgenetMinecraftGuiService();;

    @Inject
    private DependencyInjection dependencyInjection;

    @Override
    public void onEnable() {
        dependencyInjection.addDepend(minecraftGuiService);
        minecraftGuiService.registerMessageHandler(getBridgenetControl());
    }
}
