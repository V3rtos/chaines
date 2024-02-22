package me.moonways.bridgenet.api.modern_command;

import lombok.Getter;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;

public class ExecutionContext<T extends EntityCommandSender> {

    @Getter
    private final T entity;

    @Getter
    private final CommandInfo commandInfo;

    public ExecutionContext(T entity, CommandInfo commandInfo) {
        this.entity = entity;
        this.commandInfo = commandInfo;
    }
}
