package me.moonways.endpoint.gui.descriptor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class GuiDescriptor {

    private final byte id;

    private final String title;
    private final byte type;

    private final byte rows;

    private final ItemDescriptor[] itemDescriptors;
}
