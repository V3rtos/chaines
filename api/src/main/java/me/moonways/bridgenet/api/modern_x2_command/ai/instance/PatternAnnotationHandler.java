package me.moonways.bridgenet.api.modern_x2_command.ai.instance;


import me.moonways.bridgenet.api.modern_x2_command.Pattern;
import me.moonways.bridgenet.api.modern_x2_command.Patterns;
import me.moonways.bridgenet.api.modern_x2_command.ai.AICommandContext;
import me.moonways.bridgenet.api.modern_x2_command.ai.AICommandHandler;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateRequest;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateResult;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.label.CommandLabelContext;

public class PatternAnnotationHandler extends AICommandHandler<Patterns> {

    @Override
    public void prepare(AICommandContext<Patterns> context) {
    }

    @Override
    public AICommandValidateResult validate(AICommandValidateRequest<Patterns> request) {
        EntityCommandSender entity = request.getExecutionContext().getEntity();

        for (Pattern pattern : request.getCommandContext().getAnnotation().value()) {
            if (matches(request.getExecutionContext().getLabel(), pattern)) {
                return AICommandValidateResult.fail(entity, pattern.exception());
            }
        }

        return AICommandValidateResult.ok();
    }

    private boolean matches(CommandLabelContext labelContext, Pattern pattern) {
        int position = pattern.position();
        String format = pattern.value();

        CommandLabelContext.Arguments arguments = labelContext.getArguments();

        return arguments.get(position).isPresent() && arguments.get(position).get().matches(format);
    }
}
