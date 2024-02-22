package me.moonways.bridgenet.api.modern_x2_command.ai.validate;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_x2_command.Command;
import me.moonways.bridgenet.api.modern_x2_command.ExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.ai.AICommandService;
import me.moonways.bridgenet.api.modern_x2_command.ai.AINativeExecutionContext;

import java.util.List;
import java.util.stream.Collectors;

public class AICommandValidateManagement {

    @Inject
    private AICommandService commandService;

    public AICommandValidateResult validate(ExecutionContext context, Command command) {
        List<AICommandValidateResult> results = getFailedResults(context, command);

        return results.stream().findFirst().orElse(AICommandValidateResult.ok());
    }

    private List<AICommandValidateResult> getFailedResults(ExecutionContext context, Command command) {
        return commandService.validateAll(AINativeExecutionContext.create(context.getEntity(), context.getLabel(), command))
                .stream()
                .filter(AICommandValidateResult::isOk)
                .collect(Collectors.toList());
    }
}
