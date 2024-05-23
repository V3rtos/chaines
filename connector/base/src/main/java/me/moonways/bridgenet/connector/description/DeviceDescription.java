package me.moonways.bridgenet.connector.description;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.service.bus.HandshakePropertiesConst;

import java.util.Properties;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class DeviceDescription {

    private final String name;
    private final String host;

    private final int port;

    public Properties toProperties() {
        Properties properties = new Properties();

        properties.setProperty(HandshakePropertiesConst.SERVER_NAME, getName());
        properties.setProperty(HandshakePropertiesConst.SERVER_ADDRESS_HOST, getHost());
        properties.setProperty(HandshakePropertiesConst.SERVER_ADDRESS_PORT, Integer.toString(getPort()));

        return properties;
    }
}
