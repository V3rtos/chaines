package me.moonways.bridgenet.client.spigot.bukkit.event;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.client.api.minecraft.server.MinecraftCommandsEngine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@RequiredArgsConstructor
public class PlayerCommandsListener implements Listener {

    private final MinecraftCommandsEngine commandsEngine;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void handle(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (commandsEngine.isExportable(event.getMessage())) {
            event.setCancelled(
                    commandsEngine.exportCommand(player.getUniqueId(), event.getMessage()));
        }
    }
}
