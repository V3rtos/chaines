package me.moonways.model.players;

import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.model.servers.EntityServer;
import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface EntityPlayer extends EntityCommandSender, Remote {

    UUID getUniqueId() throws RemoteException;

    EntityServer getVelocityServer() throws RemoteException;

    EntityServer getSpigotServer() throws RemoteException;

    void redirect(@NotNull EntityServer server) throws RemoteException;
}
