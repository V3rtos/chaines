package me.moonways.endpoint.games;

import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.games.Game;
import me.moonways.bridgenet.model.games.GamesServiceModel;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public final class GamesServiceEndpoint extends AbstractEndpointDefinition implements GamesServiceModel {

    @Inject
    private GamesContainer container;
    @Inject
    private DependencyInjection injector;

    public GamesServiceEndpoint() throws RemoteException {
        super();
    }

    @Override
    public Game getGame(@NotNull UUID uuid) throws RemoteException {
        return null;
    }

    @Override
    public Game getGame(@NotNull String name) throws RemoteException {
        return null;
    }

    @Override
    public List<Game> getLoadedGames() throws RemoteException {
        return null;
    }

    @Override
    public boolean isGame(@NotNull EntityServer server) throws RemoteException {
        return false;
    }
}
