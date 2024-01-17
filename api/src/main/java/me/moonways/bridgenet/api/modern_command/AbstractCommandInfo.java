package me.moonways.bridgenet.api.modern_command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.moonways.bridgenet.api.modern_command.entity.EntityType;
import me.moonways.bridgenet.api.modern_command.interval.IntervalInfo;

@RequiredArgsConstructor
@Getter
@Setter
public abstract class AbstractCommandInfo implements StandardCommandInfo {

    private final String[] aliases;

    private String description;

    private String permission;

    private EntityType entityType;
    private IntervalInfo interval;

    @Override
    public abstract CommandType getCommandType();
}
