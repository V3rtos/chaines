package me.moonways.bridgenet.api.command.children.definition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.command.children.CommandChild;

import java.lang.reflect.Method;
import java.util.List;

@Getter
@RequiredArgsConstructor
public final class CommandProducerChild implements CommandChild {

    private final Object parent;
    private final Method method;

    private final List<String> aliases;

    private final String name, permission, usage, description;
}
