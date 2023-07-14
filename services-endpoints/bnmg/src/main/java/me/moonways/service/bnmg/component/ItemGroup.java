package me.moonways.service.bnmg.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemGroup {

    ITEM("item", new int[]{1, 2, 3, 4, 5}, new int[]{1, 2, 3, 4, 5}),
    DISPLAY("display", new int[]{1, 2}, new int[]{6, 7}),
    ACTIONS("action", null, null)
    ;

    public static ItemGroup getGroupByName(String name) {
        ItemGroup[] values = values();

        for (ItemGroup value : values) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    private final String name;

    private final int[] displayArray;
    private final int[] internalArray;

    public int toInternalIndex(int displayIndex) {
        if (displayArray.length != internalArray.length) {
            throw new IllegalStateException("display array length != internal array length for " + name());
        }

        for (int index = 0; index < displayArray.length; index++) {

            if (displayArray[index] == displayIndex) {
                return internalArray[index];
            }
        }

        return -1;
    }
}
