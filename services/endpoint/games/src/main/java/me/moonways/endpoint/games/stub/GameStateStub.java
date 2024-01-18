package me.moonways.endpoint.games.stub;

import lombok.Builder;
import lombok.Getter;
import me.moonways.bridgenet.model.games.GameState;
import me.moonways.bridgenet.model.games.GameStatus;
import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
@Builder(toBuilder = true)
public class GameStateStub extends AbstractEndpointDefinition implements GameState {

    private GameStatus status;
    private String map;

    private int maxPlayers;
    private int playersInTeam;
    private int players;
    private int spectators;

    @Override
    public boolean checkStatus(@NotNull GameStatus status) {
        return Objects.equals(status, this.status);
    }
}
