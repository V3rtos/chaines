package me.moonways.service.entities.server.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.service.api.entities.player.ConnectedEntityPlayer;
import me.moonways.bridgenet.mtp.MTPChannel;
import me.moonways.service.api.entities.server.EntityServer;
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
    private final MTPChannel bridgenetChannel;

    private final InetSocketAddress serverAddress;

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
