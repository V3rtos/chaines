package me.moonways.bridgenet.endpoint.servers;

import lombok.Synchronized;
import me.moonways.bridgenet.model.servers.EntityServer;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.*;

public final class ServersContainer {

    private final Map<UUID, EntityServer> registeredServersMap = Collections.synchronizedMap(new HashMap<>());

    private UUID newServerUUID() {
        UUID uuid = UUID.randomUUID();
        if (registeredServersMap.containsKey(uuid)) {
            return newServerUUID();
        }

        return uuid;
    }

    @Synchronized
    public UUID registerServer(@NotNull EntityServer server) {
        UUID serverKey = newServerUUID();
        registeredServersMap.put(serverKey, server);

        return serverKey;
    }

    @Synchronized
    public void unregisterServer(@NotNull UUID serverKey) {
        registeredServersMap.remove(serverKey);
    }

    public UUID getServerKey(@NotNull String serverName) {
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
}
