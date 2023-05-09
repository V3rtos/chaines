package me.moonways.bridgenet.server;

import me.moonways.bridgenet.BridgenetChannel;
import me.moonways.bridgenet.player.ConnectedPlayer;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface Server {

    String getName();

    BridgenetChannel getBridgenetChannel();

    InetSocketAddress getServerAddress();

    Collection<ConnectedPlayer> getConnectedPlayers();

    int getTotalOnline();

    CompletableFuture<Boolean> connect(@NotNull ConnectedPlayer player);
}
