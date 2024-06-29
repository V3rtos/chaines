package me.moonways.bridgenet.rest4j.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.minecraft.ChatColor;
import me.moonways.bridgenet.rest4j.Ok;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class OkPlayerStatus implements Ok {

    private final UUID id;
    private final String name;

    private final OkServer server;

    private final boolean isOnline;
}
