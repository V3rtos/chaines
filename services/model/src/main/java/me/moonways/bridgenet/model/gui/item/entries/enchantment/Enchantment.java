package me.moonways.bridgenet.model.gui.item.entries.enchantment;

import lombok.*;
import me.moonways.bridgenet.model.gui.item.entries.ItemsEntryable;
import org.jetbrains.annotations.NotNull;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Enchantment implements ItemsEntryable<EnchantmentEntry> {

    public static Enchantment get(@NotNull String namespace) {
        return new Enchantment(namespace);
    }

    private final String namespace;
    private EnchantmentEntry entry;

    @Override
    public String namespace() {
        return namespace;
    }

    @Override
    public EnchantmentEntry entry() {
        return entry;
    }

    @Override
    public void set(EnchantmentEntry entry) {
        this.entry = entry;
    }
}
