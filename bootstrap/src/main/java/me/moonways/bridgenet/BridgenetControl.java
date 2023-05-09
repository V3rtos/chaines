package me.moonways.bridgenet;

import lombok.Getter;
import me.moonways.bridgenet.command.Command;
import me.moonways.bridgenet.command.CommandContainer;
import me.moonways.bridgenet.command.CommandRegistry;
import me.moonways.bridgenet.dependencyinjection.DependencyInjection;
import me.moonways.bridgenet.dependencyinjection.Inject;
import me.moonways.bridgenet.event.EventService;
import me.moonways.bridgenet.module.ModuleContainer;
import me.moonways.bridgenet.player.PlayerManager;
import me.moonways.bridgenet.scheduler.Scheduler;
import me.moonways.bridgenet.server.ServerContainer;
import org.jetbrains.annotations.NotNull;

@Getter
public class BridgenetControl {

    @Inject
    private BridgenetConsole console;

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

    public void init() {
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
