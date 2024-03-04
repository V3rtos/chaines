package me.moonways.bridgenet.api.modern_x2_command.obj;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.obj.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.obj.label.CommandLabelContext;

@Getter
@RequiredArgsConstructor
public class ExecutionContext {

    private final EntityCommandSender sender;
    private final CommandLabelContext label;

    public static ExecutionContext create(EntityCommandSender entity, CommandLabelContext label) {
        return new ExecutionContext(entity, label);
    }
}
