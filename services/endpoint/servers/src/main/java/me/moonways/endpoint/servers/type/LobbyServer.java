package me.moonways.endpoint.servers.type;

import me.moonways.bridgenet.mtp.MTPChannel;

import java.net.InetSocketAddress;

public class LobbyServer extends SpigotServer {

    //@Getter
    //private final Game game;

    public LobbyServer(/*Game game,*/ String name, MTPChannel bridgenetChannel, InetSocketAddress serverAddress) {
        super(name, bridgenetChannel, serverAddress);
        //this.game = game;
    }
}
