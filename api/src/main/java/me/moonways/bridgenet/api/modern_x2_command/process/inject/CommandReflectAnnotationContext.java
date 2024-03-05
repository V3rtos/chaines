package me.moonways.bridgenet.api.modern_x2_command.process.inject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.objects.CommandInfo;

import java.lang.reflect.AnnotatedElement;

@Getter
@RequiredArgsConstructor
public class CommandReflectAnnotationContext {

    private final AnnotatedElement item;
    private final CommandInfo info;

    public static CommandReflectAnnotationContext create(AnnotatedElement annotatedElement, CommandInfo info) {
        return new CommandReflectAnnotationContext(annotatedElement, info);
    }
}
