package me.moonways.endpoint.gui.item;

import lombok.Getter;
import lombok.Setter;
import me.moonways.bridgenet.model.gui.item.Item;
import me.moonways.bridgenet.model.gui.item.Material;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
public class ItemStub implements Item, Serializable {

    private static final long serialVersionUID = -5999585115152443155L;

    private Material material;

    private String name;
    private String[] lore;

    private int customModelData;
    private int amount;
    private int durability;

    @Override
    public Stream<String> getLoreAsStream() {
        return Arrays.stream(lore);
    }

    @Override
    public void setLore(@Nullable List<String> lore) {
        if (lore == null) {
            lore = Collections.emptyList();
        }

        setLore(lore.toArray(new String[0]));
    }

    @Override
    public void setLore(@Nullable String[] lore) {
        this.lore = lore;
    }
}
