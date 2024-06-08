package me.moonways.bridgenet.mtp.connection.client;

import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;

public interface BridgenetNetworkClientHandler {
    String ATTRIBUTE_KEY = "bridgenet_client_handler";

    void onConnected(BridgenetNetworkChannel channel);

    void onDisconnected(BridgenetNetworkChannel channel);

    void onReconnect(BridgenetNetworkChannel channel);
}
