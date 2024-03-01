package me.moonways.bridgenet.endpoint.servers;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.endpoint.servers.players.PlayersOnServersConnectionService;
import me.moonways.bridgenet.model.bus.message.Redirect;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.bridgenet.model.players.connection.ConnectedEntityPlayer;
import me.moonways.bridgenet.model.players.connection.PlayerConnection;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.model.servers.ServerInfo;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
public class ConnectedServerStub implements EntityServer {

    @Setter
    private UUID uniqueId;

    private final ServerInfo serverInfo;
    private final BridgenetNetworkChannel channel;

    @Inject
    private PlayersServiceModel playersServiceModel;

    @Inject
    private PlayersOnServersConnectionService playersOnServersConnectionService;

    @Override
    public String getName() throws RemoteException {
        return serverInfo.getName();
    }

    @Override
    public InetSocketAddress getInetAddress() throws RemoteException {
        return serverInfo.getAddress();
    }

    @Override
    public CompletableFuture<Boolean> connectThat(@NotNull ConnectedEntityPlayer player) throws RemoteException {
        Redirect message = new Redirect(player.getUniqueId(), uniqueId);

        CompletableFuture<Redirect.Result> resultFuture
                = channel.sendAwait(Redirect.Result.class, message);

        return resultFuture.thenApply(result -> result instanceof Redirect.Success);
    }

    @Override
    public Collection<ConnectedEntityPlayer> getConnectedPlayers() throws RemoteException {
        PlayerConnection playerConnection = playersServiceModel.getPlayerConnection();
        return playersOnServersConnectionService.getPlayersOnServerByKey(uniqueId)
                .stream()
                .map(uuid -> {
                    try {
                        return playerConnection.getConnectedPlayer(uuid);
                    } catch (RemoteException exception) {
                        throw new ServersEndpointException(exception);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalOnline() throws RemoteException {
        return playersOnServersConnectionService.getPlayersOnServerByKey(uniqueId).size();
    }
}
