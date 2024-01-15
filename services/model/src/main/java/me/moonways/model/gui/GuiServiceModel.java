package me.moonways.model.gui;

import me.moonways.bridgenet.rsi.service.RemoteService;
import me.moonways.model.gui.item.ItemFactory;

import java.rmi.RemoteException;

public interface GuiServiceModel extends RemoteService {

    ItemFactory getItemFactory() throws RemoteException;
}
