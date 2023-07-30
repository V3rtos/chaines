package me.moonways.model.servers;

import me.moonways.bridgenet.mtp.MTPChannel;
import me.moonways.model.players.ConnectedEntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface EntityServer extends Remote {

    String getName() throws RemoteException;

    MTPChannel getMtpChannel() throws RemoteException;

    InetSocketAddress getInetAddress() throws RemoteException;

    Collection<ConnectedEntityPlayer> getConnectedPlayers() throws RemoteException;

    CompletableFuture<Boolean> connect(@NotNull ConnectedEntityPlayer player) throws RemoteException;

    int getTotalOnline() throws RemoteException;
}
