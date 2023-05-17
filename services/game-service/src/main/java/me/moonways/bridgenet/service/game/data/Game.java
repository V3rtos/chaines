package me.moonways.bridgenet.service.game.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Game {

    public static Game create(int id, String name) {
        return new Game(id, name);
    }

    private final int id;
    private final String name;
}
