package me.moonways.bridgenet.api.modern_x2_command.process.inject;

import lombok.Getter;
import me.moonways.bridgenet.api.modern_x2_command.objects.Command;
import me.moonways.bridgenet.api.modern_x2_command.objects.CommandExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.objects.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.objects.label.CommandLabelContext;

@Getter
public class CommandWrapAnnotationContext extends CommandExecutionContext {

    private final Command command;

    public CommandWrapAnnotationContext(EntityCommandSender entity, CommandLabelContext label, Command command) {
        super(entity, label);

        this.command = command;
    }

    public static CommandWrapAnnotationContext create(EntityCommandSender entity, CommandLabelContext labelContext, Command command) {
        return new CommandWrapAnnotationContext(entity, labelContext, command);
    }

    public CommandExecutionContext toExecutionContext() {
        return this;
    }
}
