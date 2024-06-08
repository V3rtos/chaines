package me.moonways.bridgenet.client.spigot.bukkit.event;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.client.spigot.BridgenetSpigotPlayersEngine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerConnectionListener implements Listener {

    private final BridgenetSpigotPlayersEngine spigotPlayersEngine;

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        spigotPlayersEngine.firePlayerJoin(event.getPlayer());
    }

    @EventHandler
    public void handle(PlayerQuitEvent event) {
        spigotPlayersEngine.firePlayerQuit(event.getPlayer());
    }
}
