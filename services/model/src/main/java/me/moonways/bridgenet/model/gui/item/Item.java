package me.moonways.bridgenet.model.gui.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Stream;

public interface Item extends Remote {

    Material getMaterial() throws RemoteException;

    String getName() throws RemoteException;

    String[] getLore() throws RemoteException;

    Stream<String> getLoreAsStream() throws RemoteException;

    int getCustomModelData() throws RemoteException;

    int getDurability() throws RemoteException;

    int getAmount() throws RemoteException;

    void setMaterial(@NotNull Material material) throws RemoteException;

    void setName(@Nullable String name) throws RemoteException;

    void setLore(@Nullable List<String> lore) throws RemoteException;

    void setLore(@Nullable String[] lore) throws RemoteException;

    void setCustomModelData(int customModelData) throws RemoteException;

    void setDurability(int durability) throws RemoteException;

    void setAmount(int amount) throws RemoteException;
}
