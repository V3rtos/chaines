package me.moonways.service.api.entities.player;

import me.moonways.service.api.entities.CommandSender;
import me.moonways.service.api.entities.server.EntityServer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface EntityPlayer extends CommandSender {

    UUID getUniqueId();

    EntityServer getVelocityServer();

    EntityServer getSpigotServer();

    void redirect(@NotNull EntityServer server);
}
