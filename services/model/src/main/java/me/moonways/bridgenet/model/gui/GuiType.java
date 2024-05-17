package me.moonways.bridgenet.model.gui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GuiType {

    CHEST(9, 9),
    CHEST_2_ROW(18, 9),
    CHEST_3_ROW(27, 9),
    CHEST_4_ROW(36, 9),
    CHEST_5_ROW(45, 9),
    CHEST_6_ROW(54, 9),
    WINDOW_3X3(9, 3),
    CRAFTER_3X3(9, 3),
    ANVIL(3, 1),
    BEACON(1, 1),
    BLAST_FURNACE(3, 1),
    BREWING_STAND(5, 1),
    CRAFTING(10, 3),
    ENCHANTMENT(2, 1),
    FURNACE(3, 1),
    GRINDSTONE(3, 1),
    HOPPER(5, 5),
    LECTERN(1, 1),
    LOOM(4, 4),
    MERCHANT(3, 1),
    SHULKER_BOX(27, 9),
    SMITHING(4, 1),
    SMOKER(3, 1),
    CARTOGRAPHY(3, 1),
    STONE_CUTTER(2, 1),
    ;

    private final int size;
    private final int rowSize;
}
