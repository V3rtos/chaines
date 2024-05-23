package me.moonways.bridgenet.model.service.gui.item.entries.material;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.service.gui.item.entries.ItemsEntry;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public final class MaterialEntry implements ItemsEntry {

    private final int id;
    private final int maxStackSize;
    private final int maxDamage;

    @SerializedName("edible")
    private final boolean isFood;

    private final String translationKey;
    private final String correspondingBlock;

    private final String eatingSound;
    private final String drinkingSound;
}
