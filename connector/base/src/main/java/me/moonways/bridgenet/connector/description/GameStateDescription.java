package me.moonways.bridgenet.connector.description;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.service.games.GameStatus;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class GameStateDescription {

    private ActiveGameDescription activeGame;

    private GameStatus status;

    private int spectators;
    private int players;
}
