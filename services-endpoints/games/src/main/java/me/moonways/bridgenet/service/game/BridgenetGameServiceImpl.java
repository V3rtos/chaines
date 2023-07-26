package me.moonways.bridgenet.service.game;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import me.moonways.service.api.games.BridgenetGamesService;
import me.moonways.service.api.games.data.Arena;
import me.moonways.bridgenet.api.injection.Component;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

@Component
public final class BridgenetGameServiceImpl extends UnicastRemoteObject implements BridgenetGamesService {

    private static final long serialVersionUID = -1131200912946923881L;

    //private final TIntObjectMap<Game> registeredGamesMap = TCollections.synchronizedMap(new TIntObjectHashMap<>());
    //private final TIntObjectMap<Mode> registeredModesMap = TCollections.synchronizedMap(new TIntObjectHashMap<>());

    private final TIntObjectMap<Set<Arena>> registeredArenasMap = TCollections.synchronizedMap(new TIntObjectHashMap<>());

    public BridgenetGameServiceImpl() throws RemoteException {
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
