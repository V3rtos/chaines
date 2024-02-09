package me.moonways.bridgenet.connector;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ConnectedDeviceInfo {

    private final String name;
    private final String host;
    private final int port;
}
