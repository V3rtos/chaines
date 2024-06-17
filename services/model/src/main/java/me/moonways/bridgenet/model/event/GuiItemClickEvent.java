package me.moonways.bridgenet.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.service.gui.Gui;
import me.moonways.bridgenet.model.service.gui.GuiSlot;
import me.moonways.bridgenet.model.service.gui.click.ClickType;
import me.moonways.bridgenet.model.service.gui.item.ItemStack;
import me.moonways.bridgenet.model.service.players.Player;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class GuiItemClickEvent implements Event {

    private final Player player;
    private final Gui gui;
    private final GuiSlot slot;
    private final ItemStack itemStack;
    private final ClickType clickType;
}
