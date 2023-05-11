package me.moonways.bridgenet.api.command;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.command.exception.CommandProcessNotFoundException;
import me.moonways.bridgenet.api.command.condition.CommandCondition;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class CommandHandlerWrapper implements CommandHandler {

    private final CommandCondition commandCondition;
    private final BiConsumer<String[], CommandSenderSession> commandHandler;

    @Override
    public BiConsumer<String[], CommandSenderSession> getCommandHandler() {
        validateHandler();

        return commandHandler;
    }

    private void validateHandler() {
        if (commandHandler == null) {
            throw new CommandProcessNotFoundException("Can't find command handler in wrapper");
        }
    }
}
