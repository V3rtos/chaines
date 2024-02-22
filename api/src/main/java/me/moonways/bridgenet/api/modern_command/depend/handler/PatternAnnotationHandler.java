package me.moonways.bridgenet.api.modern_command.depend.handler;

import me.moonways.bridgenet.api.modern_command.depend.Pattern;
import me.moonways.bridgenet.api.modern_command.depend.Patterns;
import me.moonways.bridgenet.api.modern_command.execution.annotation.CommandAnnotationHandler;
import me.moonways.bridgenet.api.modern_command.execution.annotation.context.AnnotationContext;
import me.moonways.bridgenet.api.modern_command.args.CommandArgument;
import me.moonways.bridgenet.api.modern_command.execution.verify.CommandVerifyResult;

public class PatternAnnotationHandler extends CommandAnnotationHandler<Patterns> {

    public PatternAnnotationHandler() {
        super(Patterns.class);
    }

    @Override
    public void modify(AnnotationContext<Patterns> context) {
        //nothing more
    }

    @Override
    public CommandVerifyResult verify(AnnotationContext<Patterns> context, CommandSession session) {
        CommandArgument commandArgument = session.getArgument();

        for (Pattern pattern : context.getAnnotation().value()) {
            if (matchesFormat(commandArgument, pattern)) {
                return CommandVerifyResult.fail(pattern.exception());
            }
        }
        return CommandVerifyResult.ok();
    }

    private boolean matchesFormat(CommandArgument commandArgument, Pattern pattern) {
        int position = pattern.position();
        String format = pattern.value();

        return commandArgument.get(position).isPresent() && commandArgument.get(position).get().matches(format);
    }
}
