package me.moonways.endpoint.games;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import me.moonways.bridgenet.api.inject.Depend;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.model.games.GamesServiceModel;
import me.moonways.model.games.data.Arena;

import java.rmi.RemoteException;
import java.util.Set;

@Depend
public final class GamesServiceEndpoint extends AbstractEndpointDefinition implements GamesServiceModel {

    private static final long serialVersionUID = -1131200912946923881L;

    //private final TIntObjectMap<Game> registeredGamesMap = TCollections.synchronizedMap(new TIntObjectHashMap<>());
    //private final TIntObjectMap<Mode> registeredModesMap = TCollections.synchronizedMap(new TIntObjectHashMap<>());

    private final TIntObjectMap<Set<Arena>> registeredArenasMap = TCollections.synchronizedMap(new TIntObjectHashMap<>());

    public GamesServiceEndpoint() throws RemoteException {
        super();
    }

    //public void registerGame(@NotNull Game game) {
   //    registeredGamesMap.put(game.getId(), game);
   //}

   //public void registerMode(@NotNull Mode mode) {
   //    registeredModesMap.put(mode.getId(), mode);
   //}

   //public void registerArena(@NotNull Game game, @NotNull Arena arena) {
   //    int gameID = game.getId();
   //    Set<Arena> arenasCollection = registeredArenasMap.get(gameID);

   //    if (arenasCollection == null) {
   //        arenasCollection = new HashSet<>();
   //    }

   //    arenasCollection.add(arena);
   //    registeredArenasMap.put(gameID, arenasCollection);
   //}

   //public void registerArena(@NotNull GameArena gameArena) {
   //    registerArena(gameArena.getGame(), gameArena.getArena());
   //}

   //public Game getRegisteredGame(int id) {
   //    return registeredGamesMap.get(id);
   //}

   //public Mode getRegisteredMode(int id) {
   //    return registeredModesMap.get(id);
   //}

   //public Set<Arena> getRegisteredArenas(int gameID) {
   //    return Collections.unmodifiableSet(registeredArenasMap.get(gameID));
   //}
}
