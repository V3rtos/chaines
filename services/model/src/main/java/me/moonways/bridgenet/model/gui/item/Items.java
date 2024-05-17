package me.moonways.bridgenet.model.gui.item;

import me.moonways.bridgenet.model.gui.item.entries.material.Material;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Items extends Remote {

    ItemStack empty() throws RemoteException;

    ItemStack empty(int amount) throws RemoteException;

    ItemStack typed(Material material) throws RemoteException;

    ItemStack named(Material material, String name) throws RemoteException;

    ItemStack item(Material material, int durability) throws RemoteException;

    ItemStack item(Material material, int durability, int amount) throws RemoteException;
}
