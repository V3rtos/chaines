package me.moonways.bridgenet.service.game.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Mode {

    public static Mode create(int id, String name) {
        return new Mode(id, name);
    }

    private final int id;
    private final String name;
}
