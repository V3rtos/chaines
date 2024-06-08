package me.moonways.endpoint.gui;

import me.moonways.bridgenet.model.service.gui.item.ItemStack;
import me.moonways.bridgenet.model.service.gui.item.Items;
import me.moonways.bridgenet.model.service.gui.item.entries.material.Material;
import me.moonways.bridgenet.model.service.gui.item.types.Materials;

import java.rmi.RemoteException;

public class ItemsStub implements Items {

    @Override
    public ItemStack empty() throws RemoteException {
        return ItemStack.create()
                .material(Materials.AIR);
    }

    @Override
    public ItemStack empty(int amount) throws RemoteException {
        return empty().amount(amount);
    }

    @Override
    public ItemStack typed(Material material) throws RemoteException {
        return ItemStack.create()
                .material(material);
    }

    @Override
    public ItemStack named(Material material, String name) throws RemoteException {
        return ItemStack.create()
                .material(material)
                .name(name);
    }

    @Override
    public ItemStack item(Material material, int durability) throws RemoteException {
        return ItemStack.create()
                .material(material)
                .durability(durability);
    }

    @Override
    public ItemStack item(Material material, int durability, int amount) throws RemoteException {
        return ItemStack.create()
                .material(material)
                .durability(durability)
                .amount(amount);
    }
}
