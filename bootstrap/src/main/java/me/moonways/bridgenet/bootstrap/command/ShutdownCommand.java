package me.moonways.bridgenet.bootstrap.command;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.annotation.Alias;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.CommandParameter;
import me.moonways.bridgenet.api.command.annotation.MentorExecutor;
import me.moonways.bridgenet.api.command.option.CommandParameterOnlyConsoleUse;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.bootstrap.AppBootstrap;

@Alias("stop")
@Command("shutdown")
@CommandParameter(CommandParameterOnlyConsoleUse.class)
public class ShutdownCommand {

    @Inject
    private AppBootstrap bootstrap;

    @MentorExecutor
    public void defaultCommand(CommandSession session) {
        bootstrap.shutdown();
    }
}
