package me.moonways.bridgenet.server.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.BridgenetChannel;
import me.moonways.bridgenet.player.ConnectedPlayer;
import me.moonways.bridgenet.server.Server;
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
        return 0;
    }

    @Override
    public CompletableFuture<Boolean> connect(@NotNull ConnectedPlayer player) {
        throw new UnsupportedOperationException();
    }
}
