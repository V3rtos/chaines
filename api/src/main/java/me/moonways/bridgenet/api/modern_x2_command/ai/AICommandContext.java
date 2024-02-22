package me.moonways.bridgenet.api.modern_x2_command.ai;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.CommandInfo;

import java.lang.annotation.Annotation;

@Getter
@RequiredArgsConstructor
public class AICommandContext<T extends Annotation> {

    private final T annotation;
    private final CommandInfo info;

    public static <T extends Annotation> AICommandContext<T> create(T annotation, CommandInfo commandInfo) {
        return new AICommandContext<>(annotation, commandInfo);
    }
}
