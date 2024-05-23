package me.moonways.bridgenet.connector.description;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.bus.HandshakePropertiesConst;

import java.util.Properties;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class UserDescription {

    private final String name;

    private final UUID proxyId;
    private final UUID uniqueId;

    public Properties toProperties() {
        Properties properties = new Properties();

        properties.setProperty(HandshakePropertiesConst.USER_NAME, getName());
        properties.setProperty(HandshakePropertiesConst.USER_UUID, getUniqueId().toString());
        properties.setProperty(HandshakePropertiesConst.USER_PROXY_ID, getProxyId().toString());

        return properties;
    }
}
