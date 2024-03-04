package me.moonways.bridgenet.api.modern_x2_command.annotation.validate;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_x2_command.obj.Command;
import me.moonways.bridgenet.api.modern_x2_command.obj.ExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.annotation.AnnotationCommandService;
import me.moonways.bridgenet.api.modern_x2_command.annotation.AnnotationNativeExecutionContext;

import java.util.List;
import java.util.stream.Collectors;

@Autobind
public class AnnotationCommandValidateManagement {

    @Inject
    private AnnotationCommandService commandService;

    public AnnotationCommandValidateResult validate(ExecutionContext context, Command command) {
        List<AnnotationCommandValidateResult> results = getFailedResults(context, command);

        return results.stream().findFirst().orElse(AnnotationCommandValidateResult.ok());
    }

    private List<AnnotationCommandValidateResult> getFailedResults(ExecutionContext context, Command command) {
        return commandService.validateAll(AnnotationNativeExecutionContext.create(context.getSender(), context.getLabel(), command))
                .stream()
                .filter(AnnotationCommandValidateResult::isOk)
                .collect(Collectors.toList());
    }
}
