package me.moonways.bridgenet.api.modern_x2_command.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.objects.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.objects.label.CommandLabelContext;

@Getter
@RequiredArgsConstructor
public class CommandExecutionContext {

    public static CommandExecutionContext create(EntityCommandSender entity, CommandLabelContext label) {
        return new CommandExecutionContext(entity, label);
    }

    private final EntityCommandSender sender;
    private final CommandLabelContext label;

    public CommandLabelContext.Arguments getArguments() {
        return label.getArguments();
    }
}
