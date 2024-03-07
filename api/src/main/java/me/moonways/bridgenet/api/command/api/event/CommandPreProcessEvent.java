package me.moonways.bridgenet.api.command.api.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.api.event.cancellation.EventCancellationWrapper;
import me.moonways.bridgenet.api.command.api.uses.CommandInfo;
import me.moonways.bridgenet.api.command.api.uses.CommandExecutionContext;

@Getter
@RequiredArgsConstructor
public class CommandPreProcessEvent extends EventCancellationWrapper implements Event{

    private final CommandExecutionContext commandExecutionContext;
    private final CommandInfo commandInfo;
}
