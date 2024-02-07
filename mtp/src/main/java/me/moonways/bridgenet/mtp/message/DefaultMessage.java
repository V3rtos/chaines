package me.moonways.bridgenet.mtp.message;

import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.message.persistence.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.TransferSerializeProvider;

import java.util.Optional;
import java.util.Properties;

@ToString
@ClientMessage
@ServerMessage
@NoArgsConstructor
public class DefaultMessage {

    public static DefaultMessage empty() {
        return new DefaultMessage();
    }

    @ByteTransfer(provider = TransferSerializeProvider.class)
    private final Properties properties = new Properties();

    public final synchronized void setProperty(Object key, Object value) {
        properties.put(key, value);
    }

    public final synchronized Object getObject(Object key) {
        return properties.get(key);
    }

    public final synchronized Object getObject(Object key, Object def) {
        return properties.getOrDefault(key, def);
    }

    public final synchronized String getString(Object key) {
        return getString(key, null);
    }

    public final synchronized String getString(Object key, String def) {
        return Optional.ofNullable(getObject(key)).map(Object::toString).orElse(def);
    }

    public final synchronized int getInt(Object key) {
        return getInt(key, 0);
    }

    public final synchronized int getInt(Object key, int def) {
        return Optional.ofNullable(getString(key)).map(Integer::parseInt).orElse(def);
    }

    public final synchronized double getDouble(Object key) {
        return getDouble(key, 0d);
    }

    public final synchronized double getDouble(Object key, double def) {
        return Optional.ofNullable(getString(key)).map(Double::parseDouble).orElse(def);
    }

    public final synchronized long getLong(Object key) {
        return getLong(key, 0);
    }

    public final synchronized long getLong(Object key, long def) {
        return Optional.ofNullable(getString(key)).map(Long::parseLong).orElse(def);
    }
}
