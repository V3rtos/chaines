package me.moonways.bridgenet.api.modern_x2_command.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.api.event.cancellation.EventCancellationWrapper;
import me.moonways.bridgenet.api.modern_x2_command.obj.CommandInfo;
import me.moonways.bridgenet.api.modern_x2_command.obj.ExecutionContext;

@Getter
@RequiredArgsConstructor
public class CommandPreProcessEvent extends EventCancellationWrapper implements Event{

    private final ExecutionContext executionContext;
    private final CommandInfo commandInfo;
}
