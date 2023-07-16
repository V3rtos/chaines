package me.moonways.service.api.command;

import lombok.RequiredArgsConstructor;
import me.moonways.service.api.command.exception.CommandProcessNotFoundException;
import me.moonways.service.api.command.condition.CommandCondition;

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
