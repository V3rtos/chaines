package me.moonways.bridgenet.api.connection.server.type;

import me.moonways.bridgenet.protocol.BridgenetChannel;

import java.net.InetSocketAddress;

public class LobbyServer extends SpigotServer {

    public LobbyServer(String name, BridgenetChannel bridgenetChannel, InetSocketAddress serverAddress) {
        super(name, bridgenetChannel, serverAddress);
    }
}
