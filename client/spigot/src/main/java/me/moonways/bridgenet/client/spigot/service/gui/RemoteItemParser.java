package me.moonways.bridgenet.client.spigot.service.gui;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.model.service.gui.click.ClickType;
import me.moonways.bridgenet.model.service.gui.item.ItemFlag;
import me.moonways.bridgenet.model.service.gui.item.ItemStack;
import me.moonways.bridgenet.model.service.gui.item.entries.enchantment.Enchantment;
import me.moonways.bridgenet.model.service.gui.item.entries.material.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

/**
 * Класс RemoteItemParser предоставляет методы для преобразования предметов между 
 * форматами Bukkit и форматами BridgeNet Model.
 */
@Autobind
public class RemoteItemParser {

    /**
     * Преобразует Bukkit ItemStack в BridgeNet Model ItemStack.
     *
     * @param itemStack предмет Bukkit, который необходимо преобразовать.
     * @return преобразованный BridgeNet Model ItemStack.
     */
    public ItemStack remote(org.bukkit.inventory.ItemStack itemStack) {
        ItemStack result = new ItemStack();

        result.material(remote(itemStack.getType()));
        result.durability(itemStack.getDurability());
        result.amount(itemStack.getAmount());

        result.enchantments(itemStack.getEnchantments()
                .keySet()
                .stream()
                .map(this::remote)
                .collect(Collectors.toList()));

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            result.name(itemMeta.getDisplayName());
            result.lore(itemMeta.getLore());

            result.flags(itemMeta.getItemFlags()
                    .stream()
                    .map(this::remote)
                    .collect(Collectors.toList()));
        }

        return result;
    }

    /**
     * Преобразует BridgeNet Model ItemStack в Bukkit ItemStack.
     *
     * @param itemStack BridgeNet Model предмет, который необходимо преобразовать.
     * @return преобразованный Bukkit ItemStack.
     */
    public org.bukkit.inventory.ItemStack normalize(ItemStack itemStack) {
        org.bukkit.inventory.ItemStack result = new org.bukkit.inventory.ItemStack(
                normalize(itemStack.material()),
                itemStack.amount(),
                (byte)itemStack.durability());

        itemStack.enchantments().forEach(enchantment -> result.addEnchantment(normalize(enchantment), 1));
        ItemMeta resultItemMeta = result.getItemMeta();

        if (resultItemMeta != null) {
            resultItemMeta.setDisplayName(itemStack.name());
            resultItemMeta.setLore(itemStack.lore());

            itemStack.flags().forEach(itemFlag ->
                    resultItemMeta.addItemFlags(normalize(itemFlag)));
        }

        result.setItemMeta(resultItemMeta);
        return result;
    }

    /**
     * Преобразует Bukkit Material в BridgeNet Model Material.
     *
     * @param material материал Bukkit.
     * @return преобразованный BridgeNet Model Material.
     */
    public Material remote(org.bukkit.Material material) {
        return Material.get(material.getKey().toString());
    }

    /**
     * Преобразует BridgeNet Model Material в Bukkit Material.
     *
     * @param material BridgeNet Model материал.
     * @return преобразованный Bukkit Material.
     */
    public org.bukkit.Material normalize(Material material) {
        return org.bukkit.Material.matchMaterial(material.namespace());
    }

    /**
     * Преобразует Bukkit ItemFlag в BridgeNet Model ItemFlag.
     *
     * @param itemFlag флаг предмета Bukkit.
     * @return преобразованный BridgeNet Model ItemFlag.
     */
    public ItemFlag remote(org.bukkit.inventory.ItemFlag itemFlag) {
        return ItemFlag.valueOf(itemFlag.name());
    }

    /**
     * Преобразует BridgeNet Model ItemFlag в Bukkit ItemFlag.
     *
     * @param itemFlag BridgeNet Model флаг предмета.
     * @return преобразованный Bukkit ItemFlag.
     */
    public org.bukkit.inventory.ItemFlag normalize(ItemFlag itemFlag) {
        return org.bukkit.inventory.ItemFlag.valueOf(itemFlag.name());
    }

    /**
     * Преобразует Bukkit Enchantment в BridgeNet Model Enchantment.
     *
     * @param enchantment зачарование Bukkit.
     * @return преобразованное пользовательское Enchantment.
     */
    public Enchantment remote(org.bukkit.enchantments.Enchantment enchantment) {
        return Enchantment.get(enchantment.getKey().toString());
    }

    /**
     * Преобразует BridgeNet Model Enchantment в Bukkit Enchantment.
     *
     * @param enchantment пользовательское зачарование.
     * @return преобразованное Bukkit Enchantment.
     */
    public org.bukkit.enchantments.Enchantment normalize(Enchantment enchantment) {
        return org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.fromString(enchantment.namespace()));
    }

    /**
     * Преобразует Bukkit ClickType в BridgeNet Model ClickType.
     *
     * @param clickType тип клика Bukkit.
     * @return преобразованный BridgeNet Model ClickType.
     */
    public ClickType remote(org.bukkit.event.inventory.ClickType clickType) {
        return ClickType.valueOf(clickType.name());
    }

    /**
     * Преобразует BridgeNet Model ClickType в Bukkit ClickType.
     *
     * @param clickType BridgeNet Model тип клика.
     * @return преобразованный Bukkit ClickType.
     */
    public org.bukkit.event.inventory.ClickType normalize(ClickType clickType) {
        return org.bukkit.event.inventory.ClickType.valueOf(clickType.name());
    }
}
