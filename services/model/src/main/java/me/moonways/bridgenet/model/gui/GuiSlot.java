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

    public int get() {
        return slot;
    }

    public int x() {
        return slot % 10;
    }

    public int y() {
        return slot / 10 + 1;
    }

    public GuiSlot right(int count) {
        int right = (slot + count);
        if (right <= 0)
            throw new IllegalArgumentException("Slot value cannot be < 0");

        this.slot += right;
        return this;
    }

    public GuiSlot left(int count) {
        int left = (slot - count);
        if (left <= 0)
            throw new IllegalArgumentException("Slot value cannot be < 0");

        this.slot = left;
        return this;
    }

    public GuiSlot up(int count) {
        return left(count * 9);
    }

    public GuiSlot down(int count) {
        return right(count * 9);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public GuiSlot clone() {
        return new GuiSlot(slot);
    }
}