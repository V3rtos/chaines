package me.moonways.bridgenet.client.spigot.bukkit.event;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.client.api.BridgenetServerSync;
import me.moonways.bridgenet.client.api.data.UserDto;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@RequiredArgsConstructor
public class PlayerConnectionListener implements Listener {

    private final BridgenetServerSync bridgenet;

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        bridgenet.exportUserHandshake(
                UserDto.builder()
                        .uniqueId(player.getUniqueId())
                        .name(player.getName())
                        .proxyId(UUID.randomUUID())
                        .build());
    }

    @EventHandler
    public void handle(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        bridgenet.exportUserDisconnect(
                UserDto.builder()
                        .uniqueId(player.getUniqueId())
                        .build());
    }
}
