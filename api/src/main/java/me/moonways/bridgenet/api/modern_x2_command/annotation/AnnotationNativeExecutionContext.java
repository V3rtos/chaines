package me.moonways.bridgenet.api.modern_x2_command.annotation;

import lombok.Getter;
import me.moonways.bridgenet.api.modern_x2_command.Command;
import me.moonways.bridgenet.api.modern_x2_command.ExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.label.CommandLabelContext;

@Getter
public class AnnotationNativeExecutionContext extends ExecutionContext {

    private Command command;

    public AnnotationNativeExecutionContext(EntityCommandSender entity, CommandLabelContext label, Command command) {
        super(entity, label);
    }

    public static AnnotationNativeExecutionContext create(EntityCommandSender entity, CommandLabelContext labelContext, Command command) {
        return new AnnotationNativeExecutionContext(entity, labelContext, command);
    }

    public ExecutionContext toCompleted() {
        return this;
    }
}
