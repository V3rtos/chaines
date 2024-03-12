package me.moonways.bridgenet.model.settings;

import lombok.*;
import me.moonways.bridgenet.api.util.minecraft.ChatColor;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SettingID<T> {

// ================================================================================================================================== //

    private static <T> SettingID<T> createID(String name, Class<T> type) {
        return new SettingID<T>(UUID.nameUUIDFromBytes(name.getBytes()), name, type);
    }

// ================================================================================================================================== //

    public static final SettingID<ChatColor> GLOWING_COLOR              = createID("glowingColor", ChatColor.class);
    public static final SettingID<Boolean> GLOWING_ALLOW                = createID("glowing", Boolean.class);

    public static final SettingID<Boolean> PERSONAL_MESSAGES_ALLOW      = createID("personalMessages", Boolean.class);

    public static final SettingID<Boolean> AUTO_FLIGHT_MODE_ON_JOIN     = createID("flightMode", Boolean.class);
    public static final SettingID<Boolean> AUTO_ANNOUNCEMENTS_AT_CHAT   = createID("chatAnnouncements", Boolean.class);

    public static final SettingID<Boolean> PARTIES_INVITATION_ALLOW     = createID("partiesInvite", Boolean.class);
    public static final SettingID<Boolean> FRIENDS_INVITATION_ALLOW     = createID("friendsInvite", Boolean.class);
    public static final SettingID<Boolean> GUILDS_INVITATION_ALLOW      = createID("guildsInvite", Boolean.class);

    public static final SettingID<Boolean> SOCIALS_VISIBILITY           = createID("socialsVisibility", Boolean.class);
    public static final SettingID<Boolean> GUILD_VISIBILITY             = createID("guildVisibility", Boolean.class);
    public static final SettingID<Boolean> FRIENDS_VISIBILITY           = createID("friendsVisibility", Boolean.class);
    public static final SettingID<Boolean> SCOREBOARD_VISIBILITY        = createID("scoreboardVisibility", Boolean.class);
    public static final SettingID<Boolean> BOSS_BAR_VISIBILITY          = createID("bossBarVisibility", Boolean.class);
    public static final SettingID<Boolean> LEVEL_VISIBILITY             = createID("levelVisibility", Boolean.class);
    public static final SettingID<Boolean> PLAYERS_AT_LOBBY_VISIBILITY  = createID("playersAtLobbyVisibility", Boolean.class);

// ================================================================================================================================== //

    private final UUID id;
    private final String name;

    private final Class<T> type;
}
