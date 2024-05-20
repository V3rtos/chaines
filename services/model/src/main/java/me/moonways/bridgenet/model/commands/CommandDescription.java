package me.moonways.bridgenet.model.commands;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public final class CommandDescription {

    private final String name;
    private final List<String> aliases;
    private final String usage;
    private final String description;
}
