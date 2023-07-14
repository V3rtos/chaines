package me.moonways.services.api.entities.server;

import me.moonways.bridgenet.protocol.BridgenetChannel;
import me.moonways.services.api.entities.player.ConnectedEntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface EntityServer {

    String getName();

    BridgenetChannel getBridgenetChannel();

    InetSocketAddress getServerAddress();

    Collection<ConnectedEntityPlayer> getConnectedPlayers();

    int getTotalOnline();

    CompletableFuture<Boolean> connect(@NotNull ConnectedEntityPlayer player);
}
