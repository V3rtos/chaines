package me.moonways.bridgenet.connector.spigot;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.connector.BridgenetServerSync;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BridgenetSpigotPlugin extends JavaPlugin implements Listener {

    private static final BridgenetSpigotConnector CONNECTOR = new BridgenetSpigotConnector();

    @Inject
    private BeansService beansService;
    @Inject
    private BridgenetCommandsExecutor bridgenetCommandsExecutor;

    @Override
    public void onEnable() {
        CONNECTOR.start(this);
        bridgenetCommandsExecutor.init(CONNECTOR);

        bindLoadedPlugins();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        BridgenetServerSync bridgenet = CONNECTOR.getBridgenetServerSync();
        bridgenet.sendServerDisconnect();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void handle(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (bridgenetCommandsExecutor.isExecutable(event.getMessage())) {
            event.setCancelled(
                    bridgenetCommandsExecutor.exportCommand(player, event.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(PluginEnableEvent event) {
        beansService.bind(event.getPlugin());
    }

    private void bindLoadedPlugins() {
        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            beansService.bind(plugin);
        }
    }
}
