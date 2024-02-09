package me.moonways.bridgenet.connector.spigot;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.connector.BridgenetServerSync;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BridgenetSpigotPlugin extends JavaPlugin implements Listener {
    private static final BridgenetSpigotConnector CONNECTOR = new BridgenetSpigotConnector();

    @Inject
    private BeansService beansService;

    @Override
    public void onEnable() {
        CONNECTOR.start(this);

        bindLoadedPlugins();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        BridgenetServerSync bridgenet = CONNECTOR.getBridgenetServerSync();
        bridgenet.sendServerDisconnect();
    }

    private void bindLoadedPlugins() {
        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            beansService.bind(plugin);
        }
    }

    @EventHandler
    public void handle(PluginEnableEvent event) {
        beansService.bind(event.getPlugin());
    }
}
