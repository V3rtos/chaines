package me.moonways.bridgenet.api.modern_x2_command.ai;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.CommandInfo;

import java.lang.reflect.AnnotatedElement;

@Getter
@RequiredArgsConstructor
public class AINativeCommandContext {

    private final AnnotatedElement item;
    private final CommandInfo info;

    public static AINativeCommandContext create(AnnotatedElement annotatedElement, CommandInfo info) {
        return new AINativeCommandContext(annotatedElement, info);
    }
}
