package me.moonways.bridgenet.mtp.connection;

import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;

import java.util.concurrent.CompletableFuture;

public interface BridgenetNetworkConnection {

    BridgenetNetworkChannel getChannel();

    BridgenetNetworkChannel bindSync();

    CompletableFuture<BridgenetNetworkChannel> bind();

    BridgenetNetworkChannel connectSync();

    CompletableFuture<BridgenetNetworkChannel> connect();

    void shutdown();
}
