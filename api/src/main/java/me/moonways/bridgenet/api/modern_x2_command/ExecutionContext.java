package me.moonways.bridgenet.api.modern_x2_command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.label.CommandLabelContext;

@Getter
@RequiredArgsConstructor
public class ExecutionContext {

    private final EntityCommandSender entity;
    private final CommandLabelContext label;

    private final CommandInfo commandInfo;

    public static ExecutionContext create(EntityCommandSender entity, CommandLabelContext label, CommandInfo commandInfo) {
        return new ExecutionContext(entity, label, commandInfo);
    }
}
