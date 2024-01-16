package me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.handler;

import me.moonways.bridgenet.api.modern_command.modern_annotation.AutoregisterCommandAnnotation;
import me.moonways.bridgenet.api.modern_command.modern_annotation.CommandAnnotationContext;
import me.moonways.bridgenet.api.modern_command.modern_annotation.CommandAnnotationProcessor;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.Description;

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
