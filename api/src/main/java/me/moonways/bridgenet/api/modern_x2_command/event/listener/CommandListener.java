package me.moonways.bridgenet.api.modern_x2_command.event.listener;

import me.moonways.bridgenet.api.modern_x2_command.CommandInfo;
import me.moonways.bridgenet.api.modern_x2_command.ExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.event.CommandPreProcessEvent;

public class CommandListener {

    public void handle(CommandPreProcessEvent event) {
        ExecutionContext executionContext = event.getExecutionContext();
        CommandInfo commandInfo = event.getCommandInfo();

        EntityCommandSender entityCommandSender = executionContext.getEntity();

        if (!entityCommandSender.hasPermission(commandInfo.getAccessKey())) {
            entityCommandSender.sendMessage("You do not have permission to dispatch this command");
            event.makeCancelled();
        }
    }
}
