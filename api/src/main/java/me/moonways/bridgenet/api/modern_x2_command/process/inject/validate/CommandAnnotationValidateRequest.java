package me.moonways.bridgenet.api.modern_x2_command.process.inject.validate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.objects.CommandInfo;
import me.moonways.bridgenet.api.modern_x2_command.objects.CommandExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.process.inject.CommandBaseAnnotationContext;

import java.lang.annotation.Annotation;

@Getter
@RequiredArgsConstructor
public class CommandAnnotationValidateRequest<T extends Annotation> {

    private final CommandBaseAnnotationContext<T> annotationContext;
    private final CommandExecutionContext executionContext;

    public static <T extends Annotation> CommandAnnotationValidateRequest<T> create(T annotation, CommandInfo commandInfo, CommandExecutionContext commandExecutionContext) {
        CommandBaseAnnotationContext<T> commandContext = new CommandBaseAnnotationContext<>(annotation, commandInfo);

        return new CommandAnnotationValidateRequest<>(commandContext, commandExecutionContext);
    }
}
