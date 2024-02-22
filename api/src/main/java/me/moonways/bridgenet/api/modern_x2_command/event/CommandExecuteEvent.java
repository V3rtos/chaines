package me.moonways.bridgenet.api.modern_x2_command.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.api.modern_x2_command.ExecutionContext;

@Getter
@RequiredArgsConstructor
public class CommandExecuteEvent implements Event {

    @Setter
    private boolean cancelled;
    private final ExecutionContext executionContext;
}
