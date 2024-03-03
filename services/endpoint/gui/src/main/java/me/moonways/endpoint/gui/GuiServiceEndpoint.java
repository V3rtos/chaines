package me.moonways.endpoint.gui;

import lombok.Getter;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;
import me.moonways.endpoint.gui.item.ItemFactoryStub;
import me.moonways.bridgenet.model.gui.GuiServiceModel;
import me.moonways.bridgenet.model.gui.item.ItemFactory;

import java.rmi.RemoteException;

public class GuiServiceEndpoint extends EndpointRemoteObject implements GuiServiceModel {

    private static final long serialVersionUID = -698118002154484440L;

    @Getter
    private final ItemFactory itemFactory = new ItemFactoryStub();

    public GuiServiceEndpoint() throws RemoteException {
        super();
    }
}
