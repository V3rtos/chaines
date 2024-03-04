package me.moonways.bridgenet.api.modern_x2_command.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.obj.CommandInfo;

import java.lang.annotation.Annotation;

@Getter
@RequiredArgsConstructor
public class AnnotationCommandContext<T extends Annotation> {

    private final T annotation;
    private final CommandInfo info;

    public static <T extends Annotation> AnnotationCommandContext<T> create(T annotation, CommandInfo commandInfo) {
        return new AnnotationCommandContext<>(annotation, commandInfo);
    }
}
