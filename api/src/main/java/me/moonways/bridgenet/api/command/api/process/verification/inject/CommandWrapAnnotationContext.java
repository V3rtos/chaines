package me.moonways.bridgenet.api.command.api.process.verification.inject;

import lombok.Getter;
import me.moonways.bridgenet.api.command.api.uses.Command;
import me.moonways.bridgenet.api.command.api.uses.CommandExecutionContext;
import me.moonways.bridgenet.api.command.api.uses.entity.EntityCommandSender;
import me.moonways.bridgenet.api.command.api.label.CommandLabelContext;

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
