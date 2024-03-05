package me.moonways.bridgenet.api.modern_x2_command.api.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.api.modern_x2_command.process.execute.CommandExecuteResult;
import me.moonways.bridgenet.api.modern_x2_command.objects.CommandExecutionContext;

@Getter
@RequiredArgsConstructor
public class CommandPostProcessEvent implements Event {

    private final CommandExecutionContext commandExecutionContext;
    private final CommandExecuteResult result;
}
