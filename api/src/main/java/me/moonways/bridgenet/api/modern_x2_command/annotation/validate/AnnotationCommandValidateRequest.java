package me.moonways.bridgenet.api.modern_x2_command.annotation.validate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.CommandInfo;
import me.moonways.bridgenet.api.modern_x2_command.ExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.annotation.AnnotationCommandContext;

import java.lang.annotation.Annotation;

@Getter
@RequiredArgsConstructor
public class AnnotationCommandValidateRequest<T extends Annotation> {

    private final AnnotationCommandContext<T> commandContext;
    private final ExecutionContext executionContext;

    public static <T extends Annotation> AnnotationCommandValidateRequest<T> create(T annotation, CommandInfo commandInfo, ExecutionContext executionContext) {
        AnnotationCommandContext<T> commandContext = new AnnotationCommandContext<>(annotation, commandInfo);

        return new AnnotationCommandValidateRequest<>(commandContext, executionContext);
    }
}
