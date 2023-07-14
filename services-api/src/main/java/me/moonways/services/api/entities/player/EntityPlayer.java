package me.moonways.services.api.entities.player;

import me.moonways.services.api.entities.CommandSender;
import me.moonways.services.api.entities.server.EntityServer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface EntityPlayer extends CommandSender {

    UUID getUniqueId();

    EntityServer getVelocityServer();

    EntityServer getSpigotServer();

    void redirect(@NotNull EntityServer server);
}
