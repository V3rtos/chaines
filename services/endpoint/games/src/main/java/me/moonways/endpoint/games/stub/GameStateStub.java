package me.moonways.endpoint.games.stub;

import lombok.Builder;
import lombok.Getter;
import me.moonways.bridgenet.model.games.GameState;
import me.moonways.bridgenet.model.games.GameStatus;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Builder(toBuilder = true)
public class GameStateStub implements GameState, Serializable {

    private final GameStatus status;
    private final String map;

    private final int maxPlayers;
    private final int playersInTeam;
    private final int players;
    private final int spectators;

    @Override
    public boolean checkStatus(@NotNull GameStatus status) {
        return Objects.equals(status, this.status);
    }
}
