package me.moonways.bridgenet.mtp.message;

import lombok.Synchronized;
import me.moonways.bridgenet.mtp.message.inject.ClientMessage;
import me.moonways.bridgenet.mtp.message.inject.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.TransferSerializeProvider;

import java.util.Properties;

@ClientMessage
@ServerMessage
public class DefaultMessage {

    public static DefaultMessage empty() {
        return new DefaultMessage();
    }

    @ByteTransfer(provider = TransferSerializeProvider.class)
    private final Properties properties = new Properties();

    @Synchronized
    public void addProperty(Object key, Object value) {
        properties.put(key, value);
    }

    @Synchronized
    public Object getProperty(Object key) {
        return properties.get(key);
    }

    @Synchronized
    public Object getProperty(Object key, Object def) {
        return properties.getOrDefault(key, def);
    }
}
