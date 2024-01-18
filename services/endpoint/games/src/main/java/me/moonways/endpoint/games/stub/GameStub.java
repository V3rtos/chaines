package me.moonways.endpoint.games.stub;

import lombok.Getter;
import me.moonways.bridgenet.model.games.ActiveGame;
import me.moonways.bridgenet.model.games.Game;
import me.moonways.bridgenet.model.games.GameServer;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

@Getter
public class GameStub extends AbstractEndpointDefinition implements Game {

    private final UUID uniqueId;
    private final String name;

    public GameStub(UUID uniqueId, String name) throws RemoteException {
        super();
        this.uniqueId = uniqueId;
        this.name = name;
    }

    @Override
    public List<String> getLoadedMaps() throws RemoteException {
        return null;
    }

    @Override
    public List<GameServer> getLoadedServers() throws RemoteException {
        return null;
    }

    @Override
    public List<ActiveGame> getActiveGames() throws RemoteException {
        return null;
    }

    @Override
    public List<GameServer> getLoadedServersByMap(@NotNull String map) throws RemoteException {
        return null;
    }

    @Override
    public List<ActiveGame> getActiveGamesByMap(@NotNull String map) throws RemoteException {
        return null;
    }
}
