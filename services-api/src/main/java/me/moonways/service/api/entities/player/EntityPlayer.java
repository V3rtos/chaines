package me.moonways.service.api.entities.player;

import me.moonways.service.api.entities.CommandSender;
import me.moonways.service.api.entities.server.EntityServer;
import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface EntityPlayer extends CommandSender, Remote {

    UUID getUniqueId() throws RemoteException;

    EntityServer getVelocityServer() throws RemoteException;

    EntityServer getSpigotServer() throws RemoteException;

    void redirect(@NotNull EntityServer server) throws RemoteException;
}
