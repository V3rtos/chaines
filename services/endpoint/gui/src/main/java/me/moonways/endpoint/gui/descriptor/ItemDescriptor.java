package me.moonways.endpoint.gui.descriptor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ItemDescriptor {

    private final byte slot;

    private final int type;

    private final byte data;
    private final int amount;

    private final transient ActionDescriptor[] actions;
}
