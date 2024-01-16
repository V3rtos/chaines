package me.moonways.bridgenet.api.modern_command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.moonways.bridgenet.api.modern_command.entity.EntityType;
import me.moonways.bridgenet.api.modern_command.interval.IntervalInfo;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

@RequiredArgsConstructor
@Getter
@Setter
public class SubcommandInfo implements StandardCommandInfo {

    private final Method method;

    private final String[] aliases;

    private String description;
    private String usageDescription;
    private String permission;

    private EntityType entityType;
    private IntervalInfo interval;

}
