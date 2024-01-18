package me.moonways.endpoint.games.stub;

import lombok.Getter;
import me.moonways.bridgenet.model.games.ActiveGame;
import me.moonways.bridgenet.model.games.GameServer;
import me.moonways.bridgenet.model.servers.ServerInfo;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class GameServerStub extends AbstractEndpointDefinition implements GameServer {

    private final ServerInfo serverInfo;

    private final List<ActiveGame> activeGames = new CopyOnWriteArrayList<>();

    public GameServerStub(ServerInfo serverInfo) throws RemoteException {
        super();
        this.serverInfo = serverInfo;
    }

    @Override
    public Optional<ActiveGame> getBetterGameForJoin() throws RemoteException {
        return Optional.empty();
    }
}
