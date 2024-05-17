package me.moonways.bridgenet.model.gui.item.entries;

public interface ItemsEntryable<T extends ItemsEntry> {

    String namespace();

    T entry();

    void set(T entry);
}
