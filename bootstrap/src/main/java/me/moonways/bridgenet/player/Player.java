package me.moonways.bridgenet.player;

import me.moonways.bridgenet.command.sender.Sender;
import me.moonways.bridgenet.server.type.SpigotServer;
import me.moonways.bridgenet.server.type.VelocityServer;
import org.jetbrains.annotations.NotNull;

public interface Player extends Sender {

    int getPlayerId();

    VelocityServer getVelocityServer();

    SpigotServer getSpigotServer();

    void redirect(@NotNull SpigotServer server);
}
