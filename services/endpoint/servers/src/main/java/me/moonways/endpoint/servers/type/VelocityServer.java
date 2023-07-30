package me.moonways.endpoint.servers.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.model.players.connection.ConnectedEntityPlayer;
import me.moonways.model.servers.EntityServer;
import me.moonways.bridgenet.mtp.MTPChannel;
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
    private final MTPChannel mtpChannel;

    private final InetSocketAddress inetAddress;

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
