package me.moonways.bridgenet.client.spigot;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.client.api.BridgenetServerSync;
import me.moonways.bridgenet.client.api.minecraft.server.MinecraftCommandsEngine;
import me.moonways.bridgenet.client.api.minecraft.server.MinecraftServerConnector;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BridgenetSpigotPlugin extends JavaPlugin implements Listener {

    private final MinecraftServerConnector minecraftServerConnector = new MinecraftServerConnector(this);

    @Inject
    private BeansService beansService;
    @Inject
    private MinecraftCommandsEngine commandsEngine;

    @Override
    public void onEnable() {
        minecraftServerConnector.start();

        commandsEngine.init(minecraftServerConnector);

        bindLoadedPlugins();

        Server server = getServer();
        server.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        BridgenetServerSync bridgenet = minecraftServerConnector.getBridgenetServerSync();
        bridgenet.exportClientDisconnect();
    }

    private void bindLoadedPlugins() {
        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            beansService.bind(plugin);
        }
    }

    // ---------------------------------------------- // LISTENING BUKKIT EVENTS // ---------------------------------------------- //

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(PluginEnableEvent event) {
        beansService.bind(event.getPlugin());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(PluginDisableEvent event) {
        beansService.unbind(event.getPlugin());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void handle(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (commandsEngine.isExportable(event.getMessage())) {
            event.setCancelled(
                    commandsEngine.exportCommand(player.getUniqueId(), event.getMessage()));
        }
    }

    // ---------------------------------------------- // LISTENING BUKKIT EVENTS // ---------------------------------------------- //
}
