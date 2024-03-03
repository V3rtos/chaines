package me.moonways.endpoint.games;

import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.model.games.Game;
import me.moonways.bridgenet.model.games.GameServer;
import me.moonways.bridgenet.model.games.GamesServiceModel;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.mtp.BridgenetNetworkController;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.endpoint.games.handler.GamesInputMessageListener;
import me.moonways.endpoint.games.handler.GamesServersDownstreamListener;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class GamesServiceEndpoint extends AbstractEndpointDefinition implements GamesServiceModel {

    private static final long serialVersionUID = -6262114750447856510L;

    private final GamesContainer container = new GamesContainer();

    @Inject
    private BridgenetNetworkController networkDriver;
    @Inject
    private EventService eventService;

    public GamesServiceEndpoint() throws RemoteException {
        super();
    }

    @PostConstruct
    public void init() {
        eventService.registerHandler(new GamesServersDownstreamListener(container));
        networkDriver.register(new GamesInputMessageListener(container));
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
