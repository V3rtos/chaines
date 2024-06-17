package me.moonways.bridgenet.client.spigot.service.gui;

import lombok.*;
import me.moonways.bridgenet.model.service.gui.GuiDescription;
import me.moonways.bridgenet.model.service.gui.GuiSlot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс BukkitGui представляет графический интерфейс (GUI) в Bukkit.
 */
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class BukkitGui {

    /**
     * Уникальный идентификатор GUI.
     */
    @ToString.Include
    @EqualsAndHashCode.Include
    private final UUID id;

    /**
     * Описание GUI.
     */
    @ToString.Include
    private final GuiDescription description;

    /**
     * Карта, содержащая слоты и соответствующие им предметы.
     */
    private final Map<GuiSlot, ItemStack> contentMap = new ConcurrentHashMap<>();

    /**
     * Инвентарь Bukkit, связанный с этим GUI.
     */
    private Inventory inventory;

    /**
     * Устанавливает предмет в указанный слот GUI.
     *
     * @param slot слот GUI.
     * @param itemStack предмет, который необходимо установить в слот.
     */
    public void set(GuiSlot slot, ItemStack itemStack) {
        inventory.setItem(slot.get() - 1, itemStack);
        contentMap.put(slot, itemStack);
    }

    /**
     * Возвращает предмет, находящийся в указанном слоте GUI.
     *
     * @param slot слот GUI.
     * @return предмет, находящийся в слоте, или null, если слот пуст.
     */
    public ItemStack get(GuiSlot slot) {
        return contentMap.get(slot);
    }

    /**
     * Открывает GUI для указанного игрока.
     *
     * @param player игрок, для которого открывается GUI.
     */
    public void open(Player player) {
        player.openInventory(inventory);
    }

    /**
     * Инициализирует инвентарь GUI на основе типа и размера,
     * указанных в описании GUI.
     *
     * @param holder владелец инвентаря.
     */
    public void initInventory(InventoryHolder holder) {
        switch (description.getType()) {
            case CHEST:
            case CHEST_2_ROW:
            case CHEST_3_ROW:
            case CHEST_4_ROW:
            case CHEST_5_ROW:
            case CHEST_6_ROW:
                inventory = Bukkit.createInventory(holder, description.getSize(), description.getTitle());
                break;
            default:
                InventoryType inventoryType = InventoryType.valueOf(description.getType().name());
                inventory = Bukkit.createInventory(holder, inventoryType, description.getTitle());
                break;
        }
    }
}
