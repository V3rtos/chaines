package me.moonways.bridgenet.endpoint.servers;

import me.moonways.bridgenet.api.command.CommandRegistry;
import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.endpoint.servers.command.ServersInfoCommand;
import me.moonways.bridgenet.endpoint.servers.handler.ServersDownstreamListener;
import me.moonways.bridgenet.endpoint.servers.handler.ServersInputMessagesListener;
import me.moonways.bridgenet.endpoint.servers.players.PlayersOnServersConnectionService;
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

    private static final long serialVersionUID = -1037167251538756291L;
    private final ServersContainer serversContainer = new ServersContainer();

    @Inject
    private MTPDriver mtpDriver;
    @Inject
    private BeansService beansService;
    @Inject
    private EventService eventService;
    @Inject
    private CommandRegistry commandRegistry;

    public ServersServiceEndpoint() throws RemoteException {
        super();
    }

    @PostConstruct
    public void registerAll() {
        beansService.bind(new PlayersOnServersConnectionService());
        beansService.inject(serversContainer);

        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        ServersDownstreamListener downstreamListener = new ServersDownstreamListener(serversContainer);
        ServersInputMessagesListener inputMessagesListener = new ServersInputMessagesListener(serversContainer);

        // events.
        eventService.registerHandler(downstreamListener);

        // protocol.
        mtpDriver.bindHandler(inputMessagesListener);
    }

    private void registerCommands() {
        commandRegistry.registerCommand(new ServersInfoCommand());
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
