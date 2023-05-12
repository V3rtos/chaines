package me.moonways.bridgenet.api.connection.player;

import me.moonways.bridgenet.api.command.sender.CommandSender;
import me.moonways.bridgenet.api.connection.server.type.SpigotServer;
import me.moonways.bridgenet.api.connection.server.type.VelocityServer;
import org.jetbrains.annotations.NotNull;

public interface Player extends CommandSender {

    int getPlayerId();

    VelocityServer getVelocityServer();

    SpigotServer getSpigotServer();

    void redirect(@NotNull SpigotServer server);
}
