package me.moonways.bridgenet.api.modern_command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;

@Setter
@RequiredArgsConstructor
public class Command {

    @Getter
    private final Object parent;
    @Getter
    private final Method handle;

    @Getter
    private final CommandInfo info;
}
