package me.moonways.bridgenet.command;

import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.SneakyThrows;
import me.moonways.bridgenet.command.exception.CommandNotAnnotatedException;
import me.moonways.bridgenet.dependencyinjection.Depend;

@Depend
public class CommandRegistry {

    @Getter
    private final CommandContainer commandContainer = new CommandContainer();

    @SneakyThrows
    public void register(@NotNull Class<? extends Command> commandClass) {
        String commandName = getCommandName(commandClass);

        Command command = commandClass.newInstance();
        command.setCommandName(commandName);

        command.register();

        commandContainer.addCommand(commandName, command);
    }

    private String getCommandName(@NotNull Class<? extends Command> commandClass) {
        CommandIdentifier commandIdentifier = commandClass.getAnnotation(CommandIdentifier.class);

        validateCommandIdentifier(commandClass.getName(), commandIdentifier);

        return commandIdentifier.name();
    }

    private void validateCommandIdentifier(String commandClassName, CommandIdentifier commandIdentifier) {
        if (commandIdentifier == null) {
            throw new CommandNotAnnotatedException(
                    String.format("Can't find identifier annotation in command %s", commandClassName));
        }
    }
}
