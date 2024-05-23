package me.moonways.bridgenet.model.commands.arg;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Arg {

    private final String value;

    public static Arg create(String value) {
        return new Arg(value);
    }
}
