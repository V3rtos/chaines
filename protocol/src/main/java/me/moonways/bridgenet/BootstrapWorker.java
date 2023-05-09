package me.moonways.bridgenet;

import java.util.concurrent.CompletableFuture;

public interface BootstrapWorker {

    BridgenetChannel bindSync();

    CompletableFuture<BridgenetChannel> bind();

    BridgenetChannel connectSync();

    CompletableFuture<BridgenetChannel> connect();

    void shutdownGracefully();

}
