package me.moonways.bridgenet.api.modern_x2_command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class Command {

    @Getter
    private final Object parent;
    @Getter
    private final Method handle;

    @Getter
    private final CommandInfo info;
}
