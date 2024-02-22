package me.moonways.bridgenet.api.modern_command.depend.handler;

import me.moonways.bridgenet.api.modern_command.depend.Description;
import me.moonways.bridgenet.api.modern_command.execution.annotation.CommandAnnotationHandler;
import me.moonways.bridgenet.api.modern_command.execution.annotation.context.AnnotationContext;
import me.moonways.bridgenet.api.modern_command.execution.verify.CommandVerifyResult;

public class DescriptionAnnotationHandler extends CommandAnnotationHandler<Description> {

    public DescriptionAnnotationHandler() {
        super(Description.class);
    }

    @Override
    public void modify(AnnotationContext<Description> context) {
        Description description = context.getAnnotation();

        context.getCommand().getInfo().setDescription(description.value());
    }

    @Override
    public CommandVerifyResult verify(AnnotationContext<Description> context, CommandSession session) {
        //nothing more
        return CommandVerifyResult.ok();
    }
}
