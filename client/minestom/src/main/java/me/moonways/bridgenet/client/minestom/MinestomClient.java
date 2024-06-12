package me.moonways.bridgenet.client.minestom;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.client.api.minecraft.server.MinecraftCommandsEngine;
import me.moonways.bridgenet.client.api.minecraft.server.MinecraftServerConnector;

public class MinestomClient {

    private final MinecraftServerConnector connector =
            new MinecraftServerConnector(this);

    @Inject
    private MinecraftCommandsEngine commandsEngine;

    public void enable() {
        connector.start();
        commandsEngine.init(connector);
    }

    public void shutdown() {
        connector.shutdown();
    }
}
