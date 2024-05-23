package me.moonways.bridgenet.model.service.gui.item;

import lombok.*;
import lombok.experimental.Accessors;
import me.moonways.bridgenet.model.service.gui.item.entries.enchantment.Enchantment;
import me.moonways.bridgenet.model.service.gui.item.entries.material.Material;
import me.moonways.bridgenet.model.service.gui.item.types.Materials;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public final class ItemStack {

    public static @NotNull ItemStack create() {
        ItemStack itemStack = new ItemStack();
        setDefaults(itemStack);
        return itemStack;
    }

    private static void setDefaults(@NotNull ItemStack itemStack) {
        itemStack.amount = 1;
        itemStack.material = Materials.AIR;
        itemStack.lore = new ArrayList<>();
        itemStack.flags = new ArrayList<>();
        itemStack.enchantments = new ArrayList<>();
    }

    private int amount;
    private int durability;

    private Material material;

    private String name;

    private List<String> lore;
    private List<ItemFlag> flags;
    private List<Enchantment> enchantments;

    public ItemStack amount(int amount) {
        this.amount = Math.max(1, amount);
        return this;
    }

    @Contract(" -> new")
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public @NotNull ItemStack clone() {
        return new ItemStack(amount, durability, material, name, lore, flags, enchantments);
    }
}
