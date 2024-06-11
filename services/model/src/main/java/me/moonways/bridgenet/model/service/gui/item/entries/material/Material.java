package me.moonways.bridgenet.model.service.gui.item.entries.material;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.model.service.gui.item.entries.ItemsEntryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Material implements ItemsEntryable<MaterialEntry> {

    @Contract("_ -> new")
    public static @NotNull Material get(@NotNull String namespace) {
        return new Material(namespace);
    }

    private final String namespace;
    private transient MaterialEntry entry;

    @Override
    public String namespace() {
        return namespace;
    }

    @Override
    public MaterialEntry entry() {
        return entry;
    }

    @Override
    public void set(MaterialEntry entry) {
        this.entry = entry;
    }
}
