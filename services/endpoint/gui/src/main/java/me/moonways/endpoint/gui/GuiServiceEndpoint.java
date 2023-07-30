package me.moonways.endpoint.gui;

import me.moonways.bridgenet.api.inject.PostFactoryMethod;
import me.moonways.endpoint.gui.content.BnmgContentConstructor;
import me.moonways.endpoint.gui.descriptor.GuiDescriptor;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.model.gui.GuiServiceModel;

import java.rmi.RemoteException;

public class GuiServiceEndpoint extends AbstractEndpointDefinition implements GuiServiceModel {

    private static final long serialVersionUID = -6137553585046086649L;

    private final BnmgManager manager = new BnmgManager();

    public GuiServiceEndpoint() throws RemoteException {
        super();
    }

    @PostFactoryMethod
    private void init() {
        manager.findResources();

        for (BnmgFile loadedFile : manager.getLoadedFiles()) {

            BnmgContentConstructor contentConstructor = new BnmgContentConstructor(loadedFile);
            GuiDescriptor guiDescriptor = contentConstructor.constructDescriptor();
        }
    }
}
