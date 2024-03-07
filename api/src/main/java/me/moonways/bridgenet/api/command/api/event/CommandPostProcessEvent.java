package me.moonways.bridgenet.api.command.api.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.api.command.api.process.execution.CommandExecuteResult;
import me.moonways.bridgenet.api.command.api.uses.CommandExecutionContext;

@Getter
@RequiredArgsConstructor
public class CommandPostProcessEvent implements Event {

    private final CommandExecutionContext commandExecutionContext;
    private final CommandExecuteResult result;
}
