package me.moonways.bridgenet.api.connection.server.type;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.connection.server.exception.ArenaNotFoundException;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import me.moonways.bridgenet.service.game.GameArena;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Log4j2
public class GameServer extends SpigotServer {

    private static final String ARENA_NOT_FOUND_EXCEPTION_MESSAGE = "Arena not found to deregister";

    @Getter
    //private final Game game;

    private final Map<UUID, GameArena> arenas = new ConcurrentHashMap<>();

    public GameServer(/*Game game, */String name, BridgenetChannel bridgenetChannel, InetSocketAddress serverAddress) {
        super(name, bridgenetChannel, serverAddress);
        //this.game = game;
    }

    private void validateNull(@NotNull UUID uuid) {
        if (!arenas.containsKey(uuid)) {
            throw new ArenaNotFoundException(ARENA_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    public boolean isArenaRegistered(@NotNull UUID uuid) {
        return arenas.containsKey(uuid);
    }

    public boolean addArena(@NotNull GameArena gameArena) {
        UUID arenaUUID = gameArena.getArenaUUID();
        boolean registered = isArenaRegistered(arenaUUID);

        if (!registered) {
            arenas.put(arenaUUID, gameArena);
        }

        return registered;
    }

    public void removeArena(@NotNull GameArena gameArena) {
        UUID arenaUUID = gameArena.getArenaUUID();

        validateNull(arenaUUID);
        arenas.remove(arenaUUID);
    }

   //public Set<GameArena> getArenasByModeId(int modeId) {
   //    return arenas.values().stream().filter(gameArena -> gameArena.getMode().getId() == modeId)
   //            .collect(Collectors.toSet());
   //}
}
