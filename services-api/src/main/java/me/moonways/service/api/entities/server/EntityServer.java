package me.moonways.service.api.entities.server;

import me.moonways.bridgenet.mtp.MTPChannel;
import me.moonways.service.api.entities.player.ConnectedEntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface EntityServer extends Remote {

    String getName() throws RemoteException;

    MTPChannel getBridgenetChannel() throws RemoteException;

    InetSocketAddress getServerAddress() throws RemoteException;

    Collection<ConnectedEntityPlayer> getConnectedPlayers() throws RemoteException;

    int getTotalOnline() throws RemoteException;

    CompletableFuture<Boolean> connect(@NotNull ConnectedEntityPlayer player) throws RemoteException;
}
