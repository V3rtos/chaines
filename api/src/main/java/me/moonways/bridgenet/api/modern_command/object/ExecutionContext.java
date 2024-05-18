package me.moonways.bridgenet.api.modern_command.object;

import lombok.Builder;
import lombok.Getter;
import me.moonways.bridgenet.api.modern_command.object.label.Label;

@Getter
@Builder
public class ExecutionContext {

    private final EntityCommand command;
    private final Label label;
}
