package me.moonways.service.entity.server.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.services.api.entities.player.ConnectedEntityPlayer;
import me.moonways.services.api.entities.server.EntityServer;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

@Getter
@RequiredArgsConstructor
public class VelocityServer implements EntityServer {

    private final String name;
    private final BridgenetChannel bridgenetChannel;

    private final InetSocketAddress serverAddress;

    private final Collection<ConnectedEntityPlayer> connectedPlayers = Collections.synchronizedSet(new HashSet<>());

    @Override
    public int getTotalOnline() {
        return connectedPlayers.size();
    }

    @Override
    public CompletableFuture<Boolean> connect(@NotNull ConnectedEntityPlayer player) {
        throw new UnsupportedOperationException();
    }
}
