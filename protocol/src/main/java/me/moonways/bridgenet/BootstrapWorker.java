package me.moonways.bridgenet;

public interface BootstrapWorker {

    void bindSync();

    void bind();

    void shutdownGracefully();

}
