package me.moonways.model.players.permission;

import lombok.*;
import me.moonways.bridgenet.api.util.minecraft.ChatColor;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PermissionGroup {

    public static PermissionGroup create(int id, String name, ChatColor color) {
        return new PermissionGroup(id, name, color, color, TemporalState.createDisabled());
    }

    public static PermissionGroup create(int id, String name, ChatColor color, TemporalState temporalInfo) {
        return new PermissionGroup(id, name, color, color, temporalInfo);
    }

    public static PermissionGroup create(int id, String name, ChatColor color, ChatColor plusColor) {
        return new PermissionGroup(id, name, color, plusColor, TemporalState.createDisabled());
    }

    public static PermissionGroup create(int id, String name, ChatColor color, ChatColor plusColor, TemporalState temporalInfo) {
        return new PermissionGroup(id, name, color, plusColor, temporalInfo);
    }

    private final int id;

    private final String name;

    private final ChatColor color;
    private final ChatColor plusColor;

    private final TemporalState temporalState;
}
