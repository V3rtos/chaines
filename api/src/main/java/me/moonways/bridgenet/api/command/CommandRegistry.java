package me.moonways.bridgenet.api.command;

import lombok.Getter;
import lombok.SneakyThrows;
import me.moonways.bridgenet.api.command.exception.CommandNotAnnotatedException;
import me.moonways.bridgenet.api.command.exception.CommandNotIdentifiedException;
import me.moonways.bridgenet.service.inject.Component;
import me.moonways.bridgenet.service.inject.DependencyInjection;
import me.moonways.bridgenet.service.inject.Inject;
import org.jetbrains.annotations.NotNull;

@Component
public class CommandRegistry {

    @Getter
    private final CommandContainer commandContainer = new CommandContainer();

    @Inject
    private DependencyInjection dependencyInjection;

    @SneakyThrows
    public void register(@NotNull Class<? extends Command> commandClass) {
        String commandName = getName(commandClass);

        Command command = commandClass.newInstance();
        command.setCommandName(commandName);

        command.register();

        dependencyInjection.injectDependencies(command);
        commandContainer.addCommand(commandName, command);
    }

    private String getName(@NotNull Class<? extends Command> commandClass) {
        CommandIdentifier commandIdentifier = commandClass.getAnnotation(CommandIdentifier.class);

        String commandClassName = commandClass.getName();

        validateAnnotationNull(commandClassName, commandIdentifier);
        validateAnnotationName(commandClassName, commandIdentifier);

        return commandIdentifier.name();
    }

    private void validateAnnotationNull(@NotNull String commandClassName, CommandIdentifier commandIdentifier) {
        if (commandIdentifier == null) {
            throw new CommandNotAnnotatedException(
                    String.format("Can't find identifier annotation in command %s", commandClassName));
        }
    }

    private void validateAnnotationName(@NotNull String commandClassName, @NotNull CommandIdentifier commandIdentifier) {
        if (commandIdentifier.name() == null) {
            throw new CommandNotIdentifiedException(
                    String.format("Can't find name by command %s", commandIdentifier));
        }
    }
}
