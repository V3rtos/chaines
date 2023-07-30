package me.moonways.endpoint.servers;

import lombok.Getter;
import me.moonways.endpoint.servers.type.SpigotServer;
import me.moonways.endpoint.servers.type.VelocityServer;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.model.servers.EntityServer;
import me.moonways.model.servers.ServersServiceModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public final class ServersServiceEndpoint extends AbstractEndpointDefinition implements ServersServiceModel {

    private static final long serialVersionUID = -7588970380519668412L;
    private final Map<String, EntityServer> serverMap = new HashMap<>();

    @Getter
    private final AddressServerMap addressServerMap = new AddressServerMap();

    public ServersServiceEndpoint() throws RemoteException {
        super();
    }

    private void validateNull(EntityServer server) {
        if (server == null) {
            throw new NullPointerException("server");
        }
    }

    private void validateNull(String serverName) {
        if (serverName == null) {
            throw new NullPointerException("server name");
        }
    }

    private void validateContains(String serverName) {
        if (serverMap.containsKey(serverName))
            throw new NullPointerException("server name");
    }

    public void addServer(@NotNull EntityServer server) {

        try {
            validateContains(server.getName());

            serverMap.put(server.getName().toLowerCase(), server);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        addressServerMap.registerServerAddressPort(server);
    }

    public void removeServer(@NotNull EntityServer server) {
        validateNull(server);

        try {
            serverMap.remove(server.getName().toLowerCase());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        addressServerMap.unregisterServerAddressPort(server);
    }

    @Nullable
    public EntityServer getServer(@NotNull String serverName) {
        return getUncheckedServer(serverName);
    }

    @Nullable
    public SpigotServer getSpigot(@NotNull String spigotName) {
        return getUncheckedServer(spigotName);
    }

    @Nullable
    public VelocityServer getVelocity(@NotNull String velocityName) {
        return getUncheckedServer(velocityName);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <S extends EntityServer> S getUncheckedServer(String serverName) {
        validateNull(serverName);
        return (S) serverMap.get(serverName.toLowerCase());
    }

    public boolean hasServer(@NotNull String serverName) {
        validateNull(serverName);
        return serverMap.containsKey(serverName.toLowerCase());
    }
}
