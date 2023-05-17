package me.moonways.bridgenet.service.game;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import me.moonways.bridgenet.service.game.data.Arena;
import me.moonways.bridgenet.service.game.data.Game;
import me.moonways.bridgenet.service.game.data.Mode;
import me.moonways.bridgenet.service.inject.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public final class GameService {

    private final TIntObjectMap<Game> registeredGamesMap = TCollections.synchronizedMap(new TIntObjectHashMap<>());
    private final TIntObjectMap<Mode> registeredModesMap = TCollections.synchronizedMap(new TIntObjectHashMap<>());

    private final TIntObjectMap<Set<Arena>> registeredArenasMap = TCollections.synchronizedMap(new TIntObjectHashMap<>());

    public void registerGame(@NotNull Game game) {
        registeredGamesMap.put(game.getId(), game);
    }

    public void registerMode(@NotNull Mode mode) {
        registeredModesMap.put(mode.getId(), mode);
    }

    public void registerArena(@NotNull Game game, @NotNull Arena arena) {
        int gameID = game.getId();
        Set<Arena> arenasCollection = registeredArenasMap.get(gameID);

        if (arenasCollection == null) {
            arenasCollection = new HashSet<>();
        }

        arenasCollection.add(arena);
        registeredArenasMap.put(gameID, arenasCollection);
    }

    public void registerArena(@NotNull GameArena gameArena) {
        registerArena(gameArena.getGame(), gameArena.getArena());
    }

    public Game getRegisteredGame(int id) {
        return registeredGamesMap.get(id);
    }

    public Mode getRegisteredMode(int id) {
        return registeredModesMap.get(id);
    }

    public Set<Arena> getRegisteredArenas(int gameID) {
        return Collections.unmodifiableSet(registeredArenasMap.get(gameID));
    }
}
