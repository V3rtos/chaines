package me.moonways.bridgenet.endpoint.servers;

import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.model.servers.ServerFlag;
import me.moonways.bridgenet.model.servers.ServersServiceModel;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServersServiceEndpoint extends AbstractEndpointDefinition implements ServersServiceModel {

    private final ServersContainer serversContainer = new ServersContainer();

    @Inject
    private MTPDriver mtpDriver;
    @Inject
    private DependencyInjection injector;

    public ServersServiceEndpoint() throws RemoteException {
        super();
    }

    @PostConstruct
    public void bindListeners() {
        injector.injectFields(serversContainer);
        mtpDriver.bindHandler(new ServersInputMessagesListener(serversContainer, this));
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
