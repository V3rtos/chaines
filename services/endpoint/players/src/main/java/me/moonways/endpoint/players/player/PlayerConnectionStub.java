package me.moonways.endpoint.players.player;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.event.PlayerPostRedirectEvent;
import me.moonways.bridgenet.model.service.players.Player;
import me.moonways.bridgenet.model.service.players.component.PlayerConnection;
import me.moonways.bridgenet.model.service.servers.EntityServer;
import me.moonways.bridgenet.model.service.servers.ServersServiceModel;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public final class PlayerConnectionStub implements PlayerConnection {
    private final Player player;

    @Setter
    private EntityServer currentServer;
    @Setter
    private EntityServer serverOnJoined;

    @Inject
    private ServersServiceModel serversServiceModel;

    @Override
    public CompletableFuture<PlayerPostRedirectEvent> connect(@NotNull EntityServer server) throws RemoteException {
        return server.connectThat(player).thenApply(isSuccess -> {

            if (isSuccess) {
                return PlayerPostRedirectEvent.builder()
                        .player(player)
                        .server(server)
                        .build();
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<PlayerPostRedirectEvent> connect(@NotNull UUID serverID) throws RemoteException {
        Optional<EntityServer> serverOptional = serversServiceModel.getServerExact(serverID);
        if (!serverOptional.isPresent()) {
            return CompletableFuture.completedFuture(null);
        }
        return connect(serverOptional.get());
    }

    @Override
    public Optional<EntityServer> getServer() throws RemoteException {
        return Optional.ofNullable(currentServer);
    }

    @Override
    public Optional<EntityServer> getServerOnJoined() throws RemoteException {
        return Optional.ofNullable(serverOnJoined);
    }
}
