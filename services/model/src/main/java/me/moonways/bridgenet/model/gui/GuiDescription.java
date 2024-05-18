package me.moonways.bridgenet.model.gui;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public final class GuiDescription {

    public static int toSize(int rows, GuiType type) {
        return (rows * type.getRowSize());
    }

    private final GuiType type;
    private final String title;

    private final int size;

    public int getRows() {
        return size / type.getRowSize();
    }
}
