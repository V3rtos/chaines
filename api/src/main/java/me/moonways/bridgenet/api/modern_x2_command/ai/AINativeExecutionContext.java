package me.moonways.bridgenet.api.modern_x2_command.ai;

import lombok.Getter;
import me.moonways.bridgenet.api.modern_x2_command.Command;
import me.moonways.bridgenet.api.modern_x2_command.ExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.label.CommandLabelContext;

@Getter
public class AINativeExecutionContext extends ExecutionContext {

    private Command command;

    public AINativeExecutionContext(EntityCommandSender entity, CommandLabelContext label, Command command) {
        super(entity, label);
    }

    public static AINativeExecutionContext create(EntityCommandSender entity, CommandLabelContext labelContext, Command command) {
        return new AINativeExecutionContext(entity, labelContext, command);
    }

    public ExecutionContext toCompleted() {
        return this;
    }
}
