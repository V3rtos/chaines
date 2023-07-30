package me.moonways.endpoint.servers.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.model.players.ConnectedEntityPlayer;
import me.moonways.bridgenet.mtp.MTPChannel;
import me.moonways.model.servers.EntityServer;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

@Getter
@RequiredArgsConstructor
public class SpigotServer implements EntityServer {

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
        // todo
        return new CompletableFuture<>();
    }
}
