package me.moonways.bridgenet.api.modern_command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.moonways.bridgenet.api.modern_command.entity.EntityType;
import me.moonways.bridgenet.api.modern_command.interval.IntervalInfo;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class CommandInfo implements StandardCommandInfo {

    private final Object parent;
    private final String[] aliases;

    private String permission;
    private String description;

    private EntityType entityType;

    private IntervalInfo interval;

    private List<SubcommandInfo> subcommands;
}
