package me.moonways.endpoint.players.player;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.model.players.OfflinePlayer;
import me.moonways.bridgenet.model.players.event.PlayerRedirectEvent;
import me.moonways.bridgenet.model.players.service.PlayerConnection;
import me.moonways.bridgenet.model.servers.EntityServer;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public final class PlayerConnectionStub implements PlayerConnection {
    private final OfflinePlayer player;

    @Override
    public CompletableFuture<PlayerRedirectEvent> connect(@NotNull EntityServer server) throws RemoteException {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<PlayerRedirectEvent> connect(@NotNull UUID serverID) throws RemoteException {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public Optional<EntityServer> getServer() throws RemoteException {
        return Optional.empty();
    }

    @Override
    public Optional<EntityServer> getServerOnJoined() throws RemoteException {
        return Optional.empty();
    }
}
