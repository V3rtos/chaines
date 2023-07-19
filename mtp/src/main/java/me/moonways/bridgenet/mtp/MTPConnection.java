package me.moonways.bridgenet.mtp;

import me.moonways.bridgenet.mtp.MTPChannel;

import java.util.concurrent.CompletableFuture;

public interface MTPConnection {

    MTPChannel bindSync();

    CompletableFuture<MTPChannel> bind();

    MTPChannel connectSync();

    CompletableFuture<MTPChannel> connect();

    void shutdownGracefully();

}
