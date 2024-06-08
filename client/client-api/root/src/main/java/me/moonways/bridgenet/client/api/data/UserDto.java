package me.moonways.bridgenet.client.api.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.service.bus.HandshakePropertiesConst;

import java.util.Properties;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class UserDto {

    private final String name;

    private final UUID uniqueId;
    private final UUID proxyId;

    public Properties toProperties() {
        Properties properties = new Properties();

        properties.setProperty(HandshakePropertiesConst.USER_NAME, getName());
        properties.setProperty(HandshakePropertiesConst.USER_UUID, getUniqueId().toString());
        properties.setProperty(HandshakePropertiesConst.USER_PROXY_ID, getProxyId().toString());

        return properties;
    }
}
