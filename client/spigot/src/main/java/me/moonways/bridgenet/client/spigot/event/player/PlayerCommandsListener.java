package me.moonways.bridgenet.client.spigot.event.player;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.client.api.minecraft.server.MinecraftCommandsEngine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@Log4j2
@RequiredArgsConstructor
public class PlayerCommandsListener implements Listener {

    private final MinecraftCommandsEngine commandsEngine;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        log.debug("Check command label on BridgeNet");

        if (commandsEngine.isExportable(event.getMessage())) {
            event.setCancelled(
                    commandsEngine.exportCommand(player.getUniqueId(), event.getMessage()));
        }
    }
}
