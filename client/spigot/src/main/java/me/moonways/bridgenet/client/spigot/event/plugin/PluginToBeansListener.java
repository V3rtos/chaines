package me.moonways.bridgenet.client.spigot.event.plugin;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

@RequiredArgsConstructor
public class PluginToBeansListener implements Listener {

    private final BeansService beansService;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(PluginEnableEvent event) {
        beansService.bind(event.getPlugin());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(PluginDisableEvent event) {
        beansService.unbind(event.getPlugin());
    }
}
