package me.moonways.bridgenet.model.service.permissions.group;

import lombok.*;
import me.moonways.bridgenet.api.util.minecraft.ChatColor;
import me.moonways.bridgenet.model.service.permissions.TemporalState;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PermissionGroup {
    private static final String ADDITIONAL_NAME_FEATURE = "+";

    public static PermissionGroup create(int id, String name, ChatColor color) {
        return new PermissionGroup(id, name, color, color, TemporalState.infinity());
    }

    public static PermissionGroup create(int id, String name, ChatColor color, TemporalState temporalInfo) {
        return new PermissionGroup(id, name, color, color, temporalInfo);
    }

    public static PermissionGroup create(int id, String name, ChatColor color, ChatColor plusColor) {
        return new PermissionGroup(id, name, color, plusColor, TemporalState.infinity());
    }

    public static PermissionGroup create(int id, String name, ChatColor color, ChatColor plusColor, TemporalState temporalInfo) {
        return new PermissionGroup(id, name, color, plusColor, temporalInfo);
    }

    @ToString.Include
    @EqualsAndHashCode.Include
    private final int id;

    @ToString.Include
    private final String name;

    private final ChatColor color;
    private final ChatColor plusColor;

    @ToString.Include
    private final TemporalState temporalState;

    public boolean isDefault() {
        return equals(GroupTypes.PLAYER);
    }

    public boolean isOwner() {
        return equals(GroupTypes.OWNER);
    }

    public boolean isPersonal() {
        return id > GroupTypes.PREMIUM.id;
    }

    public boolean isDonate() {
        return rangeClosedIn(GroupTypes.VIP, GroupTypes.PREMIUM);
    }

    public boolean isCommercialPersonal() {
        return rangeClosedIn(GroupTypes.YOUTUBE, GroupTypes.YOUTUBE);
    }

    public boolean isTechPersonal() {
        return rangeClosedIn(GroupTypes.TESTER, GroupTypes.ADMIN);
    }

    private boolean rangeClosedIn(PermissionGroup min, PermissionGroup max) {
        return id >= Math.min(min.id, max.id) && id <= Math.max(min.id, max.id);
    }

    public String getDisplayName() {
        if (name.contains(ADDITIONAL_NAME_FEATURE)) {
            return String.format("%s%s%s%s", color,
                    name.replace(ADDITIONAL_NAME_FEATURE, ""),
                    plusColor, ADDITIONAL_NAME_FEATURE);
        }
        return String.format("%s%s", color, name);
    }
}
