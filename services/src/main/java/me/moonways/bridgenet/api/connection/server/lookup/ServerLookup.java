package me.moonways.bridgenet.api.connection.server.lookup;

import me.moonways.bridgenet.api.connection.server.Server;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface ServerLookup<T> {

    @NotNull
    CompletableFuture<T> lookup(@NotNull BridgenetChannel channel, @NotNull Server server);
}
