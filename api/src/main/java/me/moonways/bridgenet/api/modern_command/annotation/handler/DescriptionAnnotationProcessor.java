package me.moonways.bridgenet.api.modern_command.annotation.handler;

import me.moonways.bridgenet.api.modern_command.annotation.AutoregisterCommandAnnotation;
import me.moonways.bridgenet.api.modern_command.annotation.CommandAnnotationContext;
import me.moonways.bridgenet.api.modern_command.annotation.CommandAnnotationProcessor;
import me.moonways.bridgenet.api.modern_command.annotation.value.Description;

@AutoregisterCommandAnnotation
public class DescriptionAnnotationProcessor extends CommandAnnotationProcessor<Description> {

    public DescriptionAnnotationProcessor() {
        super(Description.class);
    }

    @Override
    protected void updateCommandInfo(CommandAnnotationContext<Description> context) {
        context.update(info -> info.setDescription(context.getAnnotation().value()));
    }
}
