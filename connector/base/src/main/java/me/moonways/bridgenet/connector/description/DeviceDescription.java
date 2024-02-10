package me.moonways.bridgenet.connector.description;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class DeviceDescription {

    private final String name;
    private final String host;
    private final int port;
}
