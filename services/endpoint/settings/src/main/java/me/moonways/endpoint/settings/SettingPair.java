package me.moonways.endpoint.settings;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Builder
@ToString
@EqualsAndHashCode
public class SettingPair {

    @Getter()
    private UUID playerId;

    @Getter()
    private UUID settingId;

    @Getter()
    private Object value;
}
