package me.moonways.bridgenet.api.modern_command.execution.verify;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_command.ExecutionContext;
import me.moonways.bridgenet.api.modern_command.execution.annotation.CommandAnnotationService;

import java.util.List;
import java.util.stream.Collectors;

public class CommandVerifyManagement {

    @Inject
    private CommandAnnotationService annotationService;

    public CommandVerifyResult validate(ExecutionContext<?> context) {
        List<CommandVerifyResult> failedResults = getFailResults(context);

        if (!failedResults.isEmpty()) {
            return failedResults.stream().findFirst().orElse(CommandVerifyResult.fail());
        }

        return CommandVerifyResult.ok();
    }

    private List<CommandVerifyResult> getFailResults(ExecutionContext<?> context) {
        return annotationService.verifyAll(context).stream()
                .filter(CommandVerifyResult::isFail)
                .collect(Collectors.toList());
    }
}
