package me.moonways.bridgenet.model.service.gui.item.entries;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class GuiNamespacedEntry<T extends ItemsEntry> {

    private final String namespace;
    private final ItemsEntryable<T> instance;
}
