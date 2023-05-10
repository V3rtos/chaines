package me.moonways.bridgenet.api.connection.server.lookup;

import me.moonways.bridgenet.api.connection.server.Server;
import me.moonways.bridgenet.service.inject.Component;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@Component
public class ServerLookupService {

    private final ServerOnline totalOnlineLookup = new ServerOnline();

    private void validateNull(Server server) {
        if (server == null) {
            throw new NullPointerException("server");
        }
    }

    private void validateNull(ServerLookup<?> lookup) {
        if (lookup == null) {
            throw new NullPointerException("lookup");
        }
    }

    private <T> CompletableFuture<T> awaitLookup(Server server, ServerLookup<T> lookup) {
        validateNull(server);
        validateNull(lookup);
        return lookup.lookup(server.getBridgenetChannel(), server);
    }

    @NotNull
    public CompletableFuture<Integer> lookupServerOnline(@NotNull Server server) {
        return awaitLookup(server, totalOnlineLookup);
    }
}
