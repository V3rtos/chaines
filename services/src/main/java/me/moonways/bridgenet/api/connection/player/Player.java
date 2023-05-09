package me.moonways.bridgenet.api.connection.player;

import me.moonways.bridgenet.api.command.sender.Sender;
import me.moonways.bridgenet.api.connection.server.type.SpigotServer;
import me.moonways.bridgenet.api.connection.server.type.VelocityServer;
import org.jetbrains.annotations.NotNull;

public interface Player extends Sender {

    int getPlayerId();

    VelocityServer getVelocityServer();

    SpigotServer getSpigotServer();

    void redirect(@NotNull SpigotServer server);
}
