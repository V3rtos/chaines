package me.moonways.bnmg;

import me.moonways.bnmg.content.BnmgContentConstructor;
import me.moonways.bridgenet.api.module.AbstractModule;
import me.moonways.bridgenet.api.module.ModuleIdentifier;
import me.moonways.bridgenet.service.bnmg.BnmgFile;
import me.moonways.bridgenet.service.bnmg.BnmgService;
import me.moonways.bridgenet.service.bnmg.descriptor.GuiDescriptor;
import me.moonways.bridgenet.service.inject.Inject;

@ModuleIdentifier(id = "bnmg", name = "BridgeNetMinecraftGui", version = "1.0")
public class BridgenetMinecraftGuiModule extends AbstractModule {

    @Inject
    private BnmgService bnmgService;

    @Override
    public void onEnable() {
        bnmgService.findResources();

        for (BnmgFile loadedFile : bnmgService.getLoadedFiles()) {

            BnmgContentConstructor contentConstructor = new BnmgContentConstructor(loadedFile);
            GuiDescriptor guiDescriptor = contentConstructor.constructDescriptor();
        }
    }
}
