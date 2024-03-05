package me.moonways.bridgenet.api.modern_x2_command.api.event.listener;

import me.moonways.bridgenet.api.modern_x2_command.objects.CommandInfo;
import me.moonways.bridgenet.api.modern_x2_command.objects.CommandExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.objects.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.api.event.CommandPreProcessEvent;

public class CommandListener {

    public void handle(CommandPreProcessEvent event) {
        CommandExecutionContext commandExecutionContext = event.getCommandExecutionContext();
        CommandInfo commandInfo = event.getCommandInfo();

        EntityCommandSender entityCommandSender = commandExecutionContext.getSender();

        if (!entityCommandSender.hasPermission(commandInfo.getAccessKey())) {
            entityCommandSender.sendMessage("You do not have permission to dispatch this command");
            event.makeCancelled();
        }
    }
}
