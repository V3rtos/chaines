package me.moonways.bridgenet.api.modern_command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.moonways.bridgenet.api.modern_command.depend.cooldown.info.CooldownInfo;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntitySenderType;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Setter
@Getter
public class CommandInfo {

    private final Map<Object, Object> property = new HashMap<>();

    private final String[] aliases;

    private String description;
    private String permission;

    private EntitySenderType entityType;
    private CooldownInfo cooldown;

    private String usage;
}
