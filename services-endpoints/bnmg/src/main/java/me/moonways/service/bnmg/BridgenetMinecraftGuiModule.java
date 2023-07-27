package me.moonways.service.bnmg;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.service.bnmg.content.BnmgContentConstructor;
import me.moonways.service.bnmg.descriptor.GuiDescriptor;

public class BridgenetMinecraftGuiModule {

    @Inject
    private BnmgServiceImpl bnmgServiceImpl;

    public void onEnable() {
        bnmgServiceImpl.findResources();

        for (BnmgFile loadedFile : bnmgServiceImpl.getLoadedFiles()) {

            BnmgContentConstructor contentConstructor = new BnmgContentConstructor(loadedFile);
            GuiDescriptor guiDescriptor = contentConstructor.constructDescriptor();
        }
    }
}
