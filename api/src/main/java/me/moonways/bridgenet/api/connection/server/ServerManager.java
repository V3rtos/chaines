package me.moonways.bridgenet.api.connection.server;

import lombok.Getter;
import me.moonways.bridgenet.api.connection.server.exception.ArenaAlreadyRegisteredException;
import me.moonways.bridgenet.api.connection.server.type.SpigotServer;
import me.moonways.bridgenet.api.connection.server.type.VelocityServer;
import me.moonways.bridgenet.service.inject.Component;
import me.moonways.bridgenet.service.inject.DependencyInjection;
import me.moonways.bridgenet.service.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public final class ServerManager {

    private final Map<String, Server> serverMap = Collections.synchronizedMap(new HashMap<>());

    @Getter
    private final AddressServerMap addressServerMap = new AddressServerMap();

    @Inject
    private DependencyInjection dependencyInjection;

    private void validateNull(Server server) {
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
            throw new ArenaAlreadyRegisteredException("server name");
    }


    public void addServer(@NotNull Server server) {
        validateContains(server.getName());

        dependencyInjection.injectDependencies(server);

        serverMap.put(server.getName().toLowerCase(), server);
        addressServerMap.registerServerAddressPort(server);
    }

    public void removeServer(@NotNull Server server) {
        validateNull(server);

        serverMap.remove(server.getName().toLowerCase());
        addressServerMap.unregisterServerAddressPort(server);
    }

    @Nullable
    public Server getServerUntyped(@NotNull String serverName) {
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
    private <S extends Server> S getUncheckedServer(String serverName) {
        validateNull(serverName);
        return (S) serverMap.get(serverName.toLowerCase());
    }
}
