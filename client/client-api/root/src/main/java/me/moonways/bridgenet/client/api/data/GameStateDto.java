package me.moonways.bridgenet.client.api.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.service.games.GameStatus;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class GameStateDto {

    private ActiveGameDto activeGame;

    private GameStatus status;

    private int spectators;
    private int players;
}
