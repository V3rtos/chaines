package me.moonways.bridgenet.api.modern_x2_command.annotation;

import lombok.Getter;
import me.moonways.bridgenet.api.modern_x2_command.obj.Command;
import me.moonways.bridgenet.api.modern_x2_command.obj.ExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.obj.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.obj.label.CommandLabelContext;

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
