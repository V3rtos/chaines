package me.moonways.bridgenet.api.connection.server.type;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.connection.server.exception.ArenaAlreadyRegisteredException;
import me.moonways.bridgenet.api.connection.server.exception.ArenaNotFoundException;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Log4j2
public class GameServer extends SpigotServer {

    private static final String ARENA_REGISTERED_EXCEPTION_MESSAGE = "Arena is already registered";
    private static final String ARENA_NOT_FOUND_EXCEPTION_MESSAGE = "Arena not found to deregister";

    @Getter
    private final int gameId;

    private final Map<UUID, ArenaServer> arenas = new ConcurrentHashMap<>();

    public GameServer(int gameId, String name, BridgenetChannel bridgenetChannel, InetSocketAddress serverAddress) {
        super(name, bridgenetChannel, serverAddress);

        this.gameId = gameId;
    }

    public void validateNull(@NotNull UUID uuid) {
        if (!arenas.containsKey(uuid)) {
            throw new ArenaNotFoundException(ARENA_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    public boolean addArena(@NotNull UUID uuid, @NotNull ArenaServer arenaServer) {
        boolean registered = arenaIsRegistered(uuid);

        if (!registered) {
            arenas.put(uuid, arenaServer);
        }


        return registered;
    }

    private boolean arenaIsRegistered(@NotNull UUID uuid) {
        return arenas.containsKey(uuid);
    }

    public void removeArena(@NotNull UUID uuid) {
        validateNull(uuid);

        arenas.remove(uuid);
    }

    public Set<ArenaServer> getArenasByModeId(int modeId) {
        return arenas.values().stream().filter(arenaServer -> arenaServer.getModeId() == modeId)
                .collect(Collectors.toSet());
    }
}
