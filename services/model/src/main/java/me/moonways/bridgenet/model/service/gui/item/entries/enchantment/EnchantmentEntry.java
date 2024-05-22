package me.moonways.bridgenet.model.service.gui.item.entries.enchantment;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.service.gui.item.entries.ItemsEntry;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public final class EnchantmentEntry implements ItemsEntry {

    private final int id;
    private final int maxLevel;

    private final boolean curse;
    private final boolean treasureOnly;
    private final boolean discoverable;
    private final boolean tradeable;

    private final String translationKey;

    private final EnchantmentRarity rarity;
}
