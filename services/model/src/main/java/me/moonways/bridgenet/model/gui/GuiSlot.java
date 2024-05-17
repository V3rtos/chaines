package me.moonways.bridgenet.model.gui;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class GuiSlot {

    public static GuiSlot matrix(int x, int y) {
        return new GuiSlot(y * 9 - (9 - x));
    }

    public static GuiSlot at(int slot) {
        return new GuiSlot(slot);
    }

    public static GuiSlot first() {
        return at(1);
    }

    public static GuiSlot center(int row, GuiDescription description) {
        int rowSize = description.getType().getRowSize();
        return at(((row - 1) * rowSize) + (rowSize / 2) + 1);
    }

    public static GuiSlot center(GuiDescription description) {
        int middleRow = description.getSize() / description.getType().getRowSize();
        return center(middleRow, description);
    }

    public static GuiSlot last(GuiDescription description) {
        return at(description.getSize());
    }

    @EqualsAndHashCode.Include
    private int slot;

    public final int get() {
        return slot;
    }

    public final int x() {
        return slot % 10;
    }

    public final int y() {
        return slot / 10 + 1;
    }

    public final GuiSlot right(int count) {
        int right = (slot + count);
        if (right <= 0)
            throw new IllegalArgumentException("Slot value cannot be < 0");

        this.slot += right;
        return this;
    }

    public final GuiSlot left(int count) {
        int left = (slot - count);
        if (left <= 0)
            throw new IllegalArgumentException("Slot value cannot be < 0");

        this.slot = left;
        return this;
    }

    public final GuiSlot up(int count) {
        return left(count * 9);
    }

    public final GuiSlot down(int count) {
        return right(count * 9);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public final GuiSlot clone() {
        return new GuiSlot(slot);
    }
}