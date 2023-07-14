package me.moonways.services.api.entities.server;

import org.jetbrains.annotations.NotNull;

public interface BridgenetServers {

    void addServer(@NotNull EntityServer server);

    void removeServer(@NotNull EntityServer server);

    EntityServer getServer(@NotNull String serverName);

    boolean hasServer(@NotNull String serverName);
}
