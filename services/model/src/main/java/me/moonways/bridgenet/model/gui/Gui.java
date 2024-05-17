package me.moonways.bridgenet.model.gui;

import me.moonways.bridgenet.model.gui.click.ItemClickListener;
import me.moonways.bridgenet.model.gui.item.ItemStack;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface Gui extends Remote {

    UUID getId() throws RemoteException;

    GuiDescription getDescription() throws RemoteException;

    ItemStack getItem(GuiSlot slot) throws RemoteException;

    void setItem(GuiSlot slot, ItemStack itemStack, ItemClickListener clickListener) throws RemoteException;

    void setItem(GuiSlot slot, ItemStack itemStack) throws RemoteException;

    void removeItem(GuiSlot slot) throws RemoteException;

    void addClickListener(ItemClickListener listener) throws RemoteException;

    void addClickListener(GuiSlot slot, ItemClickListener listener) throws RemoteException;
}
