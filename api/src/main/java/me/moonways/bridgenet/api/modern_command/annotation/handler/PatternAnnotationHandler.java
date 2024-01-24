package me.moonways.bridgenet.api.modern_command.annotation.handler;

import me.moonways.bridgenet.api.modern_command.CommandAnnotationHandler;
import me.moonways.bridgenet.api.modern_command.Pattern;
import me.moonways.bridgenet.api.modern_command.Patterns;
import me.moonways.bridgenet.api.modern_command.annotation.AbstractCommandAnnotationHandler;
import me.moonways.bridgenet.api.modern_command.annotation.context.SessionAnnotationContext;
import me.moonways.bridgenet.api.modern_command.args.ArgumentWrapper;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;

@CommandAnnotationHandler
public class PatternAnnotationHandler extends AbstractCommandAnnotationHandler<Patterns> {

    public PatternAnnotationHandler() {
        super(Patterns.class);
    }

    @Override
    public Result verify(SessionAnnotationContext<Patterns> context) {
        CommandSession session = context.getSession();
        ArgumentWrapper argumentWrapper = session.getArgument();

        for (Pattern pattern : context.getAnnotation().value()) {
            if (match(argumentWrapper, pattern)) {
                return Result.error(pattern.exception());
            }
        }
        return Result.success();
    }

    private boolean match(ArgumentWrapper argumentWrapper, Pattern pattern) {
        int position = pattern.position();
        String format = pattern.value();

        return argumentWrapper.get(position).isPresent() && argumentWrapper.get(position).get().matches(format);
    }
}
