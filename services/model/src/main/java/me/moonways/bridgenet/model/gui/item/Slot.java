package me.moonways.bridgenet.model.gui.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Slot {

    public static int toIntSlot(int x, int y) {
        return y * 9 - (9 - x);
    }

    public static Slot byArray(int slot) {
        return new Slot(slot);
    }

    public static Slot first() {
        return byArray(1);
    }

    public static Slot bySlot(int slot) {
        return byArray(slot);
    }

    public static Slot byMatrixArray(int x, int y) {
        return new Slot(toIntSlot(x, y));
    }

    public static Slot byMatrix(int x, int y) {
        return byMatrixArray(x, y);
    }

    private int slot;

    public final int getAsInt() {
        return slot;
    }

    public final int asX() {
        return slot % 10;
    }

    public final int asY() {
        return slot / 10 + 1;
    }

    public final Slot right(int count) {
        int right = (slot + count);
        if (right <= 0)
            throw new IllegalArgumentException("Slot value cannot be < 0");

        this.slot += right;
        return this;
    }

    public final Slot left(int count) {
        int left = (slot - count);
        if (left <= 0)
            throw new IllegalArgumentException("Slot value cannot be < 0");

        this.slot = left;
        return this;
    }

    public final Slot up(int count) {
        return left(count * 9);
    }

    public final Slot down(int count) {
        return right(count * 9);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public final Slot clone() {
        return new Slot(slot);
    }

}