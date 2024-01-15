package me.moonways.model.players.offline;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(setterPrefix = "set", builderMethodName = "newBuilder", builderClassName = "Builder")
public class OfflineDao {

    private UUID uuid;

    private String name;

    private int experience;
    private int groupId;
}
