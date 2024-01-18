package me.moonways.bridgenet.endpoint.servers;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.moonways.bridgenet.model.bus.message.Redirect;
import me.moonways.bridgenet.model.players.connection.ConnectedEntityPlayer;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.model.servers.ServerInfo;
import me.moonways.bridgenet.mtp.MTPMessageSender;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Builder
@Getter
@ToString
public class ConnectedServerStub implements EntityServer {

    @Setter
    private UUID uniqueId;

    private final ServerInfo serverInfo;
    private final MTPMessageSender channel;

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
                = channel.sendMessageWithResponse(Redirect.Result.class, message);

        return resultFuture.thenApply(result -> result instanceof Redirect.Success);
    }

    @Override
    public Collection<ConnectedEntityPlayer> getConnectedPlayers() throws RemoteException {
        return null;
    }

    @Override
    public int getTotalOnline() throws RemoteException {
        return 0;
    }
}
