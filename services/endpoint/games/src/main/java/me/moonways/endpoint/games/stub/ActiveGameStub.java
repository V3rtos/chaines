package me.moonways.endpoint.games.stub;

import lombok.Getter;
import me.moonways.bridgenet.model.games.ActiveGame;
import me.moonways.bridgenet.model.games.GameState;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;

import java.rmi.RemoteException;
import java.util.UUID;

@Getter
public class ActiveGameStub extends AbstractEndpointDefinition implements ActiveGame {

    private final UUID uniqueId;
    private final GameState state;

    public ActiveGameStub(UUID uniqueId, GameState state) throws RemoteException {
        super();
        this.uniqueId = uniqueId;
        this.state = state;
    }

    @Override
    public String getMap() throws RemoteException {
        return state.getMap();
    }
}
