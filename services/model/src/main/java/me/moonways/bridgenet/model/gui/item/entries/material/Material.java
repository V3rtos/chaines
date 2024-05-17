package me.moonways.bridgenet.model.gui.item.entries.material;

import lombok.*;
import me.moonways.bridgenet.model.gui.item.entries.ItemsEntryable;
import org.jetbrains.annotations.NotNull;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Material implements ItemsEntryable<MaterialEntry> {

    public static Material get(@NotNull String namespace) {
        return new Material(namespace);
    }

    private final String namespace;
    private MaterialEntry entry;

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
