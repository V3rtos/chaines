package me.moonways.bridgenet.api.connection.server.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.connection.player.ConnectedPlayer;
import me.moonways.bridgenet.api.connection.server.Server;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

@Getter
@RequiredArgsConstructor
public class VelocityServer implements Server {

    private final String name;
    private final BridgenetChannel bridgenetChannel;

    private final InetSocketAddress serverAddress;

    private final Collection<ConnectedPlayer> connectedPlayers = Collections.synchronizedSet(new HashSet<>());

    @Override
    public int getTotalOnline() {
        return connectedPlayers.size();
    }

    @Override
    public CompletableFuture<Boolean> connect(@NotNull ConnectedPlayer player) {
        throw new UnsupportedOperationException();
    }
}
