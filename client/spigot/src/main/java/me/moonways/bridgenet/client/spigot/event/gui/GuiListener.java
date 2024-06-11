package me.moonways.bridgenet.client.spigot.event.gui;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.client.spigot.BridgenetSpigotGuiEngine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class GuiListener implements Listener {

    private final BridgenetSpigotGuiEngine spigotGuiEngine;

    @EventHandler
    public void handle(InventoryClickEvent event) {
        spigotGuiEngine.sendClickAction(event);
    }

    @EventHandler
    public void handle(InventoryCloseEvent event) {
        spigotGuiEngine.invalidate((Player) event.getPlayer());
    }

    @EventHandler
    public void handle(PlayerQuitEvent event) {
        spigotGuiEngine.invalidate(event.getPlayer());
    }
}
