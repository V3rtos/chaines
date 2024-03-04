package me.moonways.bridgenet.api.modern_x2_command.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.CommandInfo;

import java.lang.reflect.AnnotatedElement;

@Getter
@RequiredArgsConstructor
public class AnnotationNativeCommandContext {

    private final AnnotatedElement item;
    private final CommandInfo info;

    public static AnnotationNativeCommandContext create(AnnotatedElement annotatedElement, CommandInfo info) {
        return new AnnotationNativeCommandContext(annotatedElement, info);
    }
}
