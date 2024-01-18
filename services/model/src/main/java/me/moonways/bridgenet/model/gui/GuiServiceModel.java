package me.moonways.bridgenet.model.gui;

import me.moonways.bridgenet.model.gui.item.ItemFactory;
import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.RemoteException;

public interface GuiServiceModel extends RemoteService {

    ItemFactory getItemFactory() throws RemoteException;
}
