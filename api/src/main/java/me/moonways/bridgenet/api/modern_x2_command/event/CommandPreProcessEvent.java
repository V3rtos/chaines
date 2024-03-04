package me.moonways.bridgenet.api.modern_x2_command.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.api.event.cancellation.EventCancellationWrapper;
import me.moonways.bridgenet.api.modern_x2_command.CommandInfo;
import me.moonways.bridgenet.api.modern_x2_command.ExecutionContext;

@Getter
@RequiredArgsConstructor
public class CommandPreProcessEvent extends EventCancellationWrapper implements Event{

    private final ExecutionContext executionContext;
    private final CommandInfo commandInfo;
}
