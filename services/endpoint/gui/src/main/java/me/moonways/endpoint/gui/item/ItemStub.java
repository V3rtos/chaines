package me.moonways.endpoint.gui.item;

import lombok.Getter;
import lombok.Setter;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.model.gui.item.Item;
import me.moonways.model.gui.item.Material;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
public class ItemStub extends AbstractEndpointDefinition implements Item {

    private static final long serialVersionUID = -5999585115152443155L;

    private Material material;

    private String name;
    private String[] lore;

    private int customModelData;
    private int amount;
    private int durability;

    public ItemStub() throws RemoteException {
        super();
    }

    @Override
    public Stream<String> getLoreAsStream() throws RemoteException {
        return Arrays.stream(lore);
    }

    @Override
    public void setLore(@Nullable List<String> lore) throws RemoteException {
        if (lore == null) {
            lore = Collections.emptyList();
        }

        setLore(lore.toArray(new String[0]));
    }

    @Override
    public void setLore(@Nullable String[] lore) throws RemoteException {
        this.lore = lore;
    }
}
