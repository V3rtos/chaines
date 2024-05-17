package me.moonways.bridgenet.model.gui;

import me.moonways.bridgenet.model.gui.click.ItemClickEvent;
import me.moonways.bridgenet.model.gui.item.Items;
import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;

public interface GuiServiceModel extends RemoteService {

    Items getItems() throws RemoteException;

    Gui createGui(GuiType type) throws RemoteException;

    Gui createGui(GuiDescription description) throws RemoteException;

    Optional<Gui> getGui(UUID id) throws RemoteException;

    void fireClickAction(UUID guiId, ItemClickEvent event) throws RemoteException;

    void fireClickAction(Gui gui, ItemClickEvent event) throws RemoteException;
}
