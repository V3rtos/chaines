package me.moonways.bridgenet.client.spigot;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.client.api.BridgenetServerSync;
import me.moonways.bridgenet.client.api.minecraft.server.MinecraftCommandsEngine;
import me.moonways.bridgenet.client.api.minecraft.server.MinecraftServerConnector;
import me.moonways.bridgenet.client.spigot.bukkit.event.PlayerCommandsListener;
import me.moonways.bridgenet.client.spigot.bukkit.event.PlayerConnectionListener;
import me.moonways.bridgenet.client.spigot.bukkit.event.PluginToBeansListener;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class BridgenetSpigotPlugin extends JavaPlugin {

    private final MinecraftServerConnector connector =
            new MinecraftServerConnector(this);

    @Inject
    private BeansService beansService;
    @Inject
    private MinecraftCommandsEngine commandsEngine;

    @Override
    public void onEnable() {
        connector.start();

        commandsEngine.init(connector);

        Server server = getServer();
        beansService.bind(server);

        server.getPluginManager().registerEvents(new PluginToBeansListener(beansService), this);
        server.getPluginManager().registerEvents(new PlayerCommandsListener(commandsEngine), this);
        server.getPluginManager().registerEvents(new PlayerConnectionListener(connector.getBridgenetServerSync()), this);

        bindLoadedPlugins();
    }

    @Override
    public void onDisable() {
        BridgenetServerSync bridgenet = connector.getBridgenetServerSync();
        bridgenet.exportClientDisconnect();
    }

    private void bindLoadedPlugins() {
        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            beansService.bind(plugin);
        }
    }
}
