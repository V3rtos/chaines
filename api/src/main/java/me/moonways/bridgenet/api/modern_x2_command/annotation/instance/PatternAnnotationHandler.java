package me.moonways.bridgenet.api.modern_x2_command.annotation.instance;


import me.moonways.bridgenet.api.modern_x2_command.Pattern;
import me.moonways.bridgenet.api.modern_x2_command.Patterns;
import me.moonways.bridgenet.api.modern_x2_command.annotation.AnnotationCommandContext;
import me.moonways.bridgenet.api.modern_x2_command.annotation.AnnotationCommandHandler;
import me.moonways.bridgenet.api.modern_x2_command.annotation.validate.AnnotationCommandValidateRequest;
import me.moonways.bridgenet.api.modern_x2_command.annotation.validate.AnnotationCommandValidateResult;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.label.CommandLabelContext;

public class PatternAnnotationHandler extends AnnotationCommandHandler<Patterns> {

    @Override
    public void prepare(AnnotationCommandContext<Patterns> context) {
    }

    @Override
    public AnnotationCommandValidateResult validate(AnnotationCommandValidateRequest<Patterns> request) {
        EntityCommandSender entity = request.getExecutionContext().getEntity();

        for (Pattern pattern : request.getCommandContext().getAnnotation().value()) {
            if (matches(request.getExecutionContext().getLabel(), pattern)) {
                return AnnotationCommandValidateResult.fail(entity, pattern.exception());
            }
        }

        return AnnotationCommandValidateResult.ok();
    }

    private boolean matches(CommandLabelContext labelContext, Pattern pattern) {
        int position = pattern.position();
        String format = pattern.value();

        CommandLabelContext.Arguments arguments = labelContext.getArguments();

        return arguments.get(position).isPresent() && arguments.get(position).get().matches(format);
    }
}
