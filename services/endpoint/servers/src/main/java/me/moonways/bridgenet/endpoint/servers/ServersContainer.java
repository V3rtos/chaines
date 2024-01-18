package me.moonways.bridgenet.endpoint.servers;

import lombok.Synchronized;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.endpoint.servers.players.PlayersOnServersConnectionService;
import me.moonways.bridgenet.model.servers.ServerFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public final class ServersContainer {

    @Inject
    private DependencyInjection dependencyInjection;

    private final Map<UUID, ConnectedServerStub> registeredServersMap = Collections.synchronizedMap(new HashMap<>());

    private UUID newServerUUID() {
        UUID uuid = UUID.randomUUID();
        if (registeredServersMap.containsKey(uuid)) {
            return newServerUUID();
        }

        return uuid;
    }

    public @Nullable UUID getServerKey(@NotNull String serverName) {
        return registeredServersMap.keySet()
                .stream()
                .filter(key -> {
                    try {
                        return registeredServersMap.get(key).getName().equalsIgnoreCase(serverName);
                    } catch (RemoteException exception) {
                        throw new ServersEndpointException(exception);
                    }
                })
                .findFirst()
                .orElse(null);
    }

    @Synchronized
    public UUID registerServer(@NotNull ConnectedServerStub server) {
        dependencyInjection.injectFields(server);

        UUID serverKey = newServerUUID();
        registeredServersMap.put(serverKey, server);

        return serverKey;
    }

    @Synchronized
    public void unregisterServer(@NotNull UUID serverKey) {
        registeredServersMap.remove(serverKey);
    }

    @Synchronized
    public ConnectedServerStub getConnectedServer(@NotNull UUID serverKey) {
        return registeredServersMap.get(serverKey);
    }

    @Synchronized
    public ConnectedServerStub getConnectedServer(@NotNull String serverName) {
        return registeredServersMap.get(getServerKey(serverName));
    }

    public Stream<ConnectedServerStub> getConnectedServersWithFlag(@NotNull ServerFlag flag) {
        return registeredServersMap.values()
                .stream()
                .filter(server -> server.getServerInfo().hasFlag(flag));
    }
}
