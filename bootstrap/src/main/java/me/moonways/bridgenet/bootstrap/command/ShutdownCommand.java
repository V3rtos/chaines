package me.moonways.bridgenet.bootstrap.command;

import me.moonways.bridgenet.api.command.CommandHelper;
import me.moonways.bridgenet.api.command.GeneralCommand;
import me.moonways.bridgenet.api.command.InjectCommand;
import me.moonways.bridgenet.api.command.api.uses.CommandExecutionContext;
import me.moonways.bridgenet.api.command.api.uses.entity.EntitySenderType;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.bootstrap.AppBootstrap;

@InjectCommand
public class ShutdownCommand {

    @Inject
    private AppBootstrap bootstrap;

    @GeneralCommand({"stop", "shutdown", "exit", "close", "fail", "sd"})
    @CommandHelper(senderType = EntitySenderType.CONSOLE)
    public void defaultCommand(CommandExecutionContext executionContext) {
        bootstrap.shutdown();
    }
}
