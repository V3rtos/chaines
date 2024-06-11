package me.moonways.bridgenet.model.service.gui.click;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.service.gui.GuiSlot;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ClickAction implements Serializable {
    private static final long serialVersionUID = 8666183024650387171L;

    private final UUID playerId;
    private final UUID guiId;

    private final GuiSlot slot;

    private final ClickType clickType;
}
