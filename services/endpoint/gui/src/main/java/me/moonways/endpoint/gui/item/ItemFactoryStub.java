package me.moonways.endpoint.gui.item;

import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.model.gui.item.Item;
import me.moonways.model.gui.item.ItemFactory;
import me.moonways.model.gui.item.Material;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;

public class ItemFactoryStub extends AbstractEndpointDefinition implements ItemFactory {

    private static final long serialVersionUID = -4095210156006084798L;

    private static final int DEFAULT_DURABILITY = 0;
    private static final int DEFAULT_AMOUNT = 1;

    public ItemFactoryStub() throws RemoteException {
        super();
    }

    private Item createInitializedItem(Material material, int amount, int durability) throws RemoteException {
        Item item = new ItemStub();
        item.setMaterial(material);
        item.setAmount(amount);
        item.setDurability(durability <= 0 ? material.getDurability() : durability);
        return item;
    }

    @Override
    public Item createEmptyItem() throws RemoteException {
        return createEmptyItem(DEFAULT_AMOUNT);
    }

    @Override
    public Item createEmptyItem(int amount) throws RemoteException {
        return createInitializedItem(Material.AIR, amount, DEFAULT_DURABILITY);
    }

    @Override
    public Item createItem(@NotNull Material material) throws RemoteException {
        return createItem(material, DEFAULT_AMOUNT, DEFAULT_DURABILITY);
    }

    @Override
    public Item createItem(@NotNull Material material, int amount) throws RemoteException {
        return createItem(material, amount, DEFAULT_DURABILITY);
    }

    @Override
    public Item createItem(@NotNull Material material, int amount, int durability) throws RemoteException {
        return createInitializedItem(material, amount, durability);
    }
}
