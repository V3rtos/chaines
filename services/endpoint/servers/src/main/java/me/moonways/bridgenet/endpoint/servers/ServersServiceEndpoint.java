package me.moonways.bridgenet.endpoint.servers;

import me.moonways.bridgenet.endpoint.servers.command.ServersInfoCommand;
import me.moonways.bridgenet.endpoint.servers.handler.ServersDownstreamListener;
import me.moonways.bridgenet.endpoint.servers.handler.ServersInputMessagesListener;
import me.moonways.bridgenet.endpoint.servers.players.PlayersOnServersConnectionService;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.model.servers.ServerFlag;
import me.moonways.bridgenet.model.servers.ServersServiceModel;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteContext;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServersServiceEndpoint extends EndpointRemoteObject implements ServersServiceModel {

    private static final long serialVersionUID = -1037167251538756291L;
    private final ServersContainer serversContainer = new ServersContainer();

    public ServersServiceEndpoint() throws RemoteException {
        super();
    }

    @Override
    protected void construct(EndpointRemoteContext context) {
        context.bind(new PlayersOnServersConnectionService());
        context.registerCommand(new ServersInfoCommand());
        context.registerEventListener(new ServersDownstreamListener(serversContainer));
        context.registerMessageListener(new ServersInputMessagesListener(serversContainer));
    }

    @Override
    public List<EntityServer> getTotalServers() throws RemoteException {
        return serversContainer.getConnectedServers().collect(Collectors.toList());
    }

    @Override
    public List<EntityServer> getDefaultServers() throws RemoteException {
        return serversContainer.getConnectedServersWithFlag(ServerFlag.DEFAULT_SERVER)
                .collect(Collectors.toList());
    }

    @Override
    public List<EntityServer> getFallbackServers() throws RemoteException {
        return serversContainer.getConnectedServersWithFlag(ServerFlag.FALLBACK_SERVER)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EntityServer> getServerExact(@NotNull String serverName) throws RemoteException {
        return Optional.ofNullable(serversContainer.getConnectedServerExact(serverName));
    }

    @Override
    public Optional<EntityServer> getServerExact(@NotNull UUID uuid) throws RemoteException {
        return Optional.ofNullable(serversContainer.getConnectedServerExact(uuid));
    }

    @Override
    public Optional<EntityServer> getServer(@NotNull String serverName) throws RemoteException {
        return Optional.ofNullable(serversContainer.getConnectedServer(serverName));
    }

    @Override
    public boolean hasServer(@NotNull UUID uuid) throws RemoteException {
        return getServerExact(uuid).isPresent();
    }

    @Override
    public boolean hasServer(@NotNull String serverName) throws RemoteException {
        return getServerExact(serverName).isPresent();
    }
}
