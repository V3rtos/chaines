package me.moonways.bridgenet.api.modern_command.annotation.handler;

import me.moonways.bridgenet.api.modern_command.CommandAnnotationHandler;
import me.moonways.bridgenet.api.modern_command.Description;
import me.moonways.bridgenet.api.modern_command.annotation.AbstractCommandAnnotationHandler;
import me.moonways.bridgenet.api.modern_command.annotation.context.AnnotationContext;

@CommandAnnotationHandler
public class DescriptionAnnotationHandler extends AbstractCommandAnnotationHandler<Description> {

    public DescriptionAnnotationHandler() {
        super(Description.class);
    }

    @Override
    protected void modify(AnnotationContext<Description> context) {
        Description description = context.getAnnotation();

        context.getCommandInfo().setDescription(description.value());
    }
}
