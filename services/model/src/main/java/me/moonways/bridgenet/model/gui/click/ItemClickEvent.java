package me.moonways.bridgenet.model.gui.click;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.gui.GuiSlot;
import me.moonways.bridgenet.model.gui.item.ItemStack;
import me.moonways.bridgenet.model.players.Player;

@Getter
@Builder
@ToString
public class ItemClickEvent implements Event {

    private final Player player;
    private final GuiSlot slot;
    private final ItemStack itemStack;
    private final ClickType clickType;
}
