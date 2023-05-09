package me.moonways.bridgenet.api;

import lombok.Getter;
import me.moonways.bridgenet.api.command.Command;
import me.moonways.bridgenet.api.command.CommandContainer;
import me.moonways.bridgenet.api.command.CommandRegistry;
import me.moonways.bridgenet.api.dependencyinjection.Depend;
import me.moonways.bridgenet.api.dependencyinjection.DependencyInjection;
import me.moonways.bridgenet.api.dependencyinjection.InitMethod;
import me.moonways.bridgenet.api.dependencyinjection.Inject;
import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.module.ModuleContainer;
import me.moonways.bridgenet.api.connection.player.PlayerManager;
import me.moonways.bridgenet.api.scheduler.Scheduler;
import me.moonways.bridgenet.api.connection.server.ServerContainer;
import org.jetbrains.annotations.NotNull;

@Getter
@Depend
public class BridgenetControl {

    @Inject
    private CommandRegistry commandRegistry;

    @Inject
    private EventService eventService;

    @Inject
    private Scheduler scheduler;

    @Inject
    private DependencyInjection dependencyInjection;

    @Inject
    private ModuleContainer moduleContainer;

    @Inject
    private ServerContainer serverContainer;

    @Inject
    private PlayerManager playerManager;

    @InitMethod
    private void init() {
        moduleContainer.loadModules();
    }

    private void validateCommand(Class<?> commandType) {
        if (commandType == null) {
            throw new NullPointerException("command");
        }
    }

    public void registerCommand(@NotNull Class<? extends Command> commandClass) {
        validateCommand(commandClass);
        commandRegistry.register(commandClass);
    }

    public Command getCommand(@NotNull String line) {
        CommandContainer commandContainer = commandRegistry.getCommandContainer();
        return commandContainer.getCommand(line);
    }

}
