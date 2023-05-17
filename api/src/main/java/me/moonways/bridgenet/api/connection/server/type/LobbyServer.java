package me.moonways.bridgenet.api.connection.server.type;

import lombok.Getter;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import me.moonways.bridgenet.service.game.data.Game;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyServer extends SpigotServer {

    @Getter
    private final Game game;

    public LobbyServer(Game game, String name, BridgenetChannel bridgenetChannel, InetSocketAddress serverAddress) {
        super(name, bridgenetChannel, serverAddress);
        this.game = game;
    }
}
