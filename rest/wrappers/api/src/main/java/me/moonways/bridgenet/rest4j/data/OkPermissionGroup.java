package me.moonways.bridgenet.rest4j.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.minecraft.ChatColor;
import me.moonways.bridgenet.rest4j.Ok;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class OkPermissionGroup implements Ok {

    private final int id;

    private final String name;

    private final ChatColor chatColor;
    private final ChatColor plusColor;
}
