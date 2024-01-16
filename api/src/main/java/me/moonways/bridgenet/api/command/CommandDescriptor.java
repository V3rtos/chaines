package me.moonways.bridgenet.api.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class CommandDescriptor {

    private final String name;
    private final String permission;
    private final String usage;
    private final String description;
    private final List<String> aliases;
}
