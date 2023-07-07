package me.moonways.bridgenet.api.connection.server.type;

import lombok.Getter;
import me.moonways.bridgenet.protocol.BridgenetChannel;

import java.net.InetSocketAddress;

public class LobbyServer extends SpigotServer {

    //@Getter
    //private final Game game;

    public LobbyServer(/*Game game,*/ String name, BridgenetChannel bridgenetChannel, InetSocketAddress serverAddress) {
        super(name, bridgenetChannel, serverAddress);
        //this.game = game;
    }
}
