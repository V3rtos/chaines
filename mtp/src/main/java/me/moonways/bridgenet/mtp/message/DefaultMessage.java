package me.moonways.bridgenet.mtp.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.message.persistence.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.TransferSerializeProvider;

import java.util.Properties;

@ToString
@ClientMessage
@ServerMessage
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultMessage {

    public static DefaultMessage empty() {
        return new DefaultMessage();
    }

    @ByteTransfer(provider = TransferSerializeProvider.class)
    private final Properties properties = new Properties();

    public final synchronized void setProperty(Object key, Object value) {
        properties.put(key, value);
    }

    public final synchronized Object getProperty(Object key) {
        return properties.get(key);
    }

    public final synchronized Object getProperty(Object key, Object def) {
        return properties.getOrDefault(key, def);
    }
}
