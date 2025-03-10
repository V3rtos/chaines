package me.moonways.bridgenet.model.service.permissions.group;

import me.moonways.bridgenet.api.minecraft.ChatColor;
import me.moonways.bridgenet.model.service.permissions.TemporalState;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public interface GroupTypes {

// ====================================================================================================================================== //

    /**
     * Default player group.
     */
    PermissionGroup PLAYER = PermissionGroup.create(0, "", ChatColor.GRAY);

    /**
     * Donate privileges.
     */
    PermissionGroup VIP = PermissionGroup.create(1, "VIP", ChatColor.YELLOW);
    PermissionGroup VIP_PLUS = PermissionGroup.create(2, "VIP+", ChatColor.YELLOW, ChatColor.GREEN);
    PermissionGroup MVP = PermissionGroup.create(3, "MVP", ChatColor.AQUA);
    PermissionGroup MVP_PLUS = PermissionGroup.create(4, "MVP+", ChatColor.AQUA, ChatColor.LIGHT_PURPLE);
    PermissionGroup PREMIUM = PermissionGroup.create(5, "PREMIUM", ChatColor.GOLD,
            TemporalState.enabled(Duration.ofDays(30))); // Monthly subscription.
    /**
     * Commercial Personal.
     */
    PermissionGroup YOUTUBE = PermissionGroup.create(10, "YOUTUBE", ChatColor.GOLD);

    /**
     * Basically Personal.
     */
    PermissionGroup HELPER = PermissionGroup.create(20, "HELPER", ChatColor.DARK_GREEN);
    PermissionGroup HELPER_PLUS = PermissionGroup.create(21, "HELPER+", ChatColor.DARK_GREEN);

    /**
     * Tech Personal.
     */
    PermissionGroup TESTER = PermissionGroup.create(30, "TESTER", ChatColor.GREEN);
    PermissionGroup TESTER_PLUS = PermissionGroup.create(30, "TESTER+", ChatColor.GREEN, ChatColor.RED);
    PermissionGroup DEVELOPER = PermissionGroup.create(31, "DEV", ChatColor.BLUE);
    PermissionGroup DEVELOPER_PLUS = PermissionGroup.create(31, "DEV+", ChatColor.BLUE, ChatColor.RED);
    PermissionGroup ADMIN = PermissionGroup.create(50, "ADMIN", ChatColor.RED);

    /**
     * Highest personal (SEO).
     */
    PermissionGroup OWNER = PermissionGroup.create(100, "OWNER", ChatColor.DARK_RED);

// ====================================================================================================================================== //

    /**
     * Ordered list of all groups
     */
    List<PermissionGroup> ORDERED_GROUPS_LIST = Arrays.asList(
            PLAYER,
            VIP, VIP_PLUS, MVP, MVP_PLUS, PREMIUM,
            YOUTUBE,
            HELPER, HELPER_PLUS,
            TESTER, TESTER_PLUS, DEVELOPER, DEVELOPER_PLUS, ADMIN,
            OWNER
    );

// ====================================================================================================================================== //
}
