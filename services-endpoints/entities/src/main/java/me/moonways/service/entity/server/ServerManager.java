package me.moonways.service.entity.server;

import lombok.Getter;
import me.moonways.service.entity.server.type.SpigotServer;
import me.moonways.service.entity.server.type.VelocityServer;
import me.moonways.bridgenet.service.inject.Component;
import me.moonways.services.api.entities.server.BridgenetServers;
import me.moonways.services.api.entities.server.EntityServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public final class ServerManager implements BridgenetServers {

    private final Map<String, EntityServer> serverMap = Collections.synchronizedMap(new HashMap<>());

    @Getter
    private final AddressServerMap addressServerMap = new AddressServerMap();

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
        validateContains(server.getName());

        serverMap.put(server.getName().toLowerCase(), server);
        addressServerMap.registerServerAddressPort(server);
    }

    public void removeServer(@NotNull EntityServer server) {
        validateNull(server);

        serverMap.remove(server.getName().toLowerCase());
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

    public boolean hasServer(String serverName) {
        validateNull(serverName);
        return serverMap.containsKey(serverName.toLowerCase());
    }
}
