package me.moonways.endpoint.games.stub;

import lombok.Builder;
import lombok.Getter;
import me.moonways.bridgenet.model.games.GameState;
import me.moonways.bridgenet.model.games.GameStatus;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.Objects;

@Getter
public class GameStateStub extends AbstractEndpointDefinition implements GameState {

    private final GameStatus status;
    private final String map;

    private final int maxPlayers;
    private final int playersInTeam;
    private final int players;
    private final int spectators;

    @Builder(toBuilder = true)
    public GameStateStub(GameStatus status, String map,
                         int maxPlayers,
                         int playersInTeam,
                         int players,
                         int spectators) throws RemoteException {
        super();
        this.status = status;
        this.map = map;
        this.maxPlayers = maxPlayers;
        this.playersInTeam = playersInTeam;
        this.players = players;
        this.spectators = spectators;
    }

    @Override
    public boolean checkStatus(@NotNull GameStatus status) {
        return Objects.equals(status, this.status);
    }
}
