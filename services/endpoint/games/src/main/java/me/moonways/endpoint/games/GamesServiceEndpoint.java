package me.moonways.endpoint.games;

import me.moonways.bridgenet.model.service.games.Game;
import me.moonways.bridgenet.model.service.games.GameServer;
import me.moonways.bridgenet.model.service.games.GamesServiceModel;
import me.moonways.bridgenet.model.service.servers.EntityServer;
import me.moonways.bridgenet.rmi.endpoint.persistance.EndpointRemoteContext;
import me.moonways.bridgenet.rmi.endpoint.persistance.EndpointRemoteObject;
import me.moonways.endpoint.games.handler.GamesInputMessageListener;
import me.moonways.endpoint.games.handler.GamesServersDownstreamListener;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class GamesServiceEndpoint extends EndpointRemoteObject implements GamesServiceModel {

    private static final long serialVersionUID = -6262114750447856510L;

    private final GamesContainer container = new GamesContainer();

    public GamesServiceEndpoint() throws RemoteException {
        super();
    }

    @Override
    protected void construct(EndpointRemoteContext context) {
        context.registerEventListener(new GamesServersDownstreamListener(container));
        context.registerMessageListener(new GamesInputMessageListener(container));
    }

    @Override
    public Game getGame(@NotNull UUID uuid) throws RemoteException {
        return container.getGame(uuid);
    }

    @Override
    public Game getGame(@NotNull String name) throws RemoteException {
        return container.getGameByName(name);
    }

    @Override
    public List<Game> getLoadedGames() throws RemoteException {
        return Collections.unmodifiableList(container.getCollectedGames());
    }

    @Override
    public boolean isGame(@NotNull EntityServer server) throws RemoteException {
        List<Game> loadedGamesList = getLoadedGames();

        for (Game game : loadedGamesList) {
            for (GameServer gameServer : game.getLoadedServers()) {

                if (gameServer.getServerInfo().getName().equalsIgnoreCase(server.getName())) {
                    return true;
                }
            }
        }

        return false;
    }
}
