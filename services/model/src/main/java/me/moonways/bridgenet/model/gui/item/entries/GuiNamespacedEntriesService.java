package me.moonways.bridgenet.model.gui.item.entries;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.model.gui.item.entries.enchantment.EnchantmentEntry;
import me.moonways.bridgenet.model.gui.item.entries.material.MaterialEntry;
import me.moonways.bridgenet.model.gui.item.types.Enchantments;
import me.moonways.bridgenet.model.gui.item.types.Materials;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Autobind
public final class GuiNamespacedEntriesService {

    @Inject
    private BeansService beansService;

    @PostConstruct
    private void init() throws IllegalAccessException {
        GuiNamespacedEntriesLoader<MaterialEntry> materialsLoader = new GuiNamespacedEntriesLoader<>("items.json", MaterialEntry.class);
        GuiNamespacedEntriesLoader<EnchantmentEntry> enchantmentsLoader = new GuiNamespacedEntriesLoader<>("enchantments.json", EnchantmentEntry.class);

        beansService.inject(materialsLoader);
        beansService.inject(enchantmentsLoader);

        materialsLoader.load(getEntries(Materials.class));
        enchantmentsLoader.load(getEntries(Enchantments.class));
    }

    @SuppressWarnings("unchecked")
    private static <T extends ItemsEntry> List<GuiNamespacedEntry<T>> getEntries(Class<?> entriesListClass) throws IllegalAccessException {
        List<GuiNamespacedEntry<T>> list = new ArrayList<>();

        for (Field field : entriesListClass.getDeclaredFields()) {
            if (ItemsEntryable.class.isAssignableFrom(field.getType())) {

                ItemsEntryable<T> entryable = (ItemsEntryable<T>) field.get(null);
                list.add(new GuiNamespacedEntry<>(entryable.namespace(), entryable));
            }
        }

        return list;
    }
}
