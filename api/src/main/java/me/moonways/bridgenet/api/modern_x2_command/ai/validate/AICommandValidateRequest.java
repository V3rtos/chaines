package me.moonways.bridgenet.api.modern_x2_command.ai.validate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.CommandInfo;
import me.moonways.bridgenet.api.modern_x2_command.ExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.ai.AICommandContext;

import java.lang.annotation.Annotation;

@Getter
@RequiredArgsConstructor
public class AICommandValidateRequest<T extends Annotation> {

    private final AICommandContext<T> commandContext;
    private final ExecutionContext executionContext;

    public static <T extends Annotation> AICommandValidateRequest<T> create(T annotation, CommandInfo commandInfo, ExecutionContext executionContext) {
        AICommandContext<T> commandContext = new AICommandContext<>(annotation, commandInfo);

        return new AICommandValidateRequest<>(commandContext, executionContext);
    }
}
