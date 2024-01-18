package me.moonways.bridgenet.endpoint.servers;

import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.model.servers.ServersServiceModel;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.UUID;

public class ServersServiceEndpoint extends AbstractEndpointDefinition implements ServersServiceModel {

    private final ServersContainer serversContainer = new ServersContainer();

    @Inject
    private MTPDriver mtpDriver;

    public ServersServiceEndpoint() throws RemoteException {
        super();
    }

    @PostConstruct
    public void bindListeners() {
        mtpDriver.bindHandler(new ServersInputMessagesListener(serversContainer));
    }

    @Override
    public EntityServer getServer(@NotNull UUID uuid) throws RemoteException {
        return null;
    }

    @Override
    public EntityServer getServer(@NotNull String serverName) throws RemoteException {
        return null;
    }

    @Override
    public boolean hasServer(@NotNull UUID uuid) throws RemoteException {
        return false;
    }

    @Override
    public boolean hasServer(@NotNull String serverName) throws RemoteException {
        return false;
    }
}
