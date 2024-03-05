package me.moonways.bridgenet.api.modern_x2_command.process.annotation.impl;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_x2_command.CommandArg;
import me.moonways.bridgenet.api.modern_x2_command.ComandArgHelper;
import me.moonways.bridgenet.api.modern_x2_command.CommandRegexId;
import me.moonways.bridgenet.api.modern_x2_command.process.annotation.CommandBaseAnnotationContext;
import me.moonways.bridgenet.api.modern_x2_command.process.annotation.CommandAnnotationHandler;
import me.moonways.bridgenet.api.modern_x2_command.process.annotation.validate.CommandAnnotationValidateRequest;
import me.moonways.bridgenet.api.modern_x2_command.process.annotation.validate.CommandAnnotationValidateResult;
import me.moonways.bridgenet.api.modern_x2_command.objects.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.objects.label.CommandLabelContext;
import me.moonways.bridgenet.api.modern_x2_command.objects.regex.CommandRegex;
import me.moonways.bridgenet.api.modern_x2_command.objects.regex.CommandRegexRegistry;

import java.util.ArrayList;
import java.util.List;

public class CommandAnnotationArgumentHandler extends CommandAnnotationHandler<ComandArgHelper> {

    @Inject
    private CommandRegexRegistry regexRegistry;

    @Override
    public void prepare(CommandBaseAnnotationContext<ComandArgHelper> context) {
    }

    @Override
    public CommandAnnotationValidateResult validate(CommandAnnotationValidateRequest<ComandArgHelper> request) {
        ComandArgHelper comandArgHelper = request.getAnnotationContext().getAnnotation();
        CommandArg[] syntaxes = request.getAnnotationContext().getAnnotation().value();

        CommandLabelContext labelContext = request.getExecutionContext().getLabel();
        CommandLabelContext.Arguments arguments = labelContext.getArguments();
        EntityCommandSender sender = request.getExecutionContext().getSender();

        List<CommandAnnotationValidateResult> failedResults = new ArrayList<>();

        for (CommandArg syntax : syntaxes) {
            int position = syntax.position();
            CommandRegexId annotationCommandRegexId = syntax.regexId();

            if (!hasPosition(arguments, position)) {
                return CommandAnnotationValidateResult.fail(sender, combineUsageAndDescription(comandArgHelper));
            }

            String regexId = annotationCommandRegexId.value();
            if (regexId.isEmpty()) {
                continue;
            }

            CommandAnnotationValidateResult validatedResult = validateResult(sender, arguments, position, annotationCommandRegexId.value());
            if (!validatedResult.isOk()) {
                failedResults.add(validatedResult);
            }

            return failedResults.isEmpty() ? CommandAnnotationValidateResult.ok() : failedResults.stream().findFirst().get();
        }

        return CommandAnnotationValidateResult.ok();
    }

    private String combineUsageAndDescription(ComandArgHelper helper) {
        return helper.usage() + " - " + helper.description();
    }

    private CommandAnnotationValidateResult validateResult(EntityCommandSender sender, CommandLabelContext.Arguments arguments,
                                                             int position, String regexId) {
        if (hasRegex(regexId)) {
            CommandRegex commandRegex = regexRegistry.get(regexId);

            if (!matches(arguments, position, commandRegex.getValue())) {
                return CommandAnnotationValidateResult.fail(sender, commandRegex.getExceptionMsg());
            }
        }
        return CommandAnnotationValidateResult.ok();
    }

    private boolean matches(CommandLabelContext.Arguments arguments, int position, String format) {
        return arguments.get(position).get().matches(format);
    }

    private boolean hasRegex(String id) {
        return regexRegistry.contains(id);
    }

    private boolean hasPosition(CommandLabelContext.Arguments arguments, int position) {
        return arguments.has(position);
    }
}
