package me.moonways.bridgenet.command;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.command.exception.CommandProcessNotFoundException;
import me.moonways.bridgenet.command.condition.CommandCondition;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class CommandExecutorWrapper implements CommandExecutor {

    private final CommandCondition commandCondition;
    private final BiConsumer<String[], CommandExecutorSession> commandConsumer;

    @Override
    public BiConsumer<String[], CommandExecutorSession> getCommandConsumer() {
        validateProcessNull();

        return commandConsumer;
    }

    private void validateProcessNull() {
        if (commandConsumer == null) {
            throw new CommandProcessNotFoundException("Can't find command process in wrapper");
        }
    }
}
