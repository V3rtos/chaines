package me.moonways.model.gui.item;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ItemFactory extends Remote {

    Item createEmptyItem() throws RemoteException;

    Item createEmptyItem(int amount) throws RemoteException;

    Item createItem(@NotNull Material material) throws RemoteException;

    Item createItem(@NotNull Material material, int amount) throws RemoteException;

    Item createItem(@NotNull Material material, int amount, int durability) throws RemoteException;
}
