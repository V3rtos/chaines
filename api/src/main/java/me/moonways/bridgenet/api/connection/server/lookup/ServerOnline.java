package me.moonways.bridgenet.api.connection.server.lookup;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.api.connection.server.Server;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ServerOnline implements ServerLookup<Integer> {

    @Override
    public @NotNull CompletableFuture<Integer> lookup(@NotNull BridgenetChannel channel, @NotNull Server server) {
        // todo
        throw new UnsupportedOperationException();
    }
}
