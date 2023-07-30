package me.moonways.model.players.offline;

import lombok.*;
import net.conveno.jdbc.response.ConvenoResponseLine;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(setterPrefix = "set", builderMethodName = "newBuilder", builderClassName = "Builder")
public class OfflinePlayerData {

    private static final int UUID_RESPONSE_INDEX = 1;
    private static final int NAME_RESPONSE_INDEX = 1;
    private static final int EXPERIENCE_RESPONSE_INDEX = 1;
    private static final int GROUP_RESPONSE_INDEX = 1;

    private UUID uuid;

    private String name;

    private int experience;
    private int groupLevel;

    public static OfflinePlayerData createEmpty() {
        return new OfflinePlayerData();
    }

    public static OfflinePlayerData create(UUID uuid, String name, int experience, int groupLevel) {
        return new OfflinePlayerData(uuid, name, experience, groupLevel);
    }

    public static OfflinePlayerData readResponse(ConvenoResponseLine response) {
        if (response == null || response.isEmpty()) {
            throw new NullPointerException("response");
        }

        UUID uuid = UUID.fromString(response.getNullableString(UUID_RESPONSE_INDEX));

        String name = response.getNullableString(NAME_RESPONSE_INDEX);

        int experience = response.getNullableInt(EXPERIENCE_RESPONSE_INDEX);
        int groupLevel = response.getNullableInt(GROUP_RESPONSE_INDEX);

        return new OfflinePlayerData(uuid, name, experience, groupLevel);
    }
}
