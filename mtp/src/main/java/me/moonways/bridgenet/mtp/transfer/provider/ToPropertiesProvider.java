package me.moonways.bridgenet.mtp.transfer.provider;

import io.netty.buffer.ByteBuf;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;

import java.util.Properties;
import java.util.Set;

public class ToPropertiesProvider implements TransferProvider {

    private void validateType(Class<?> cls) {
        if (!Properties.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException(cls + " is not assignable from java.util.Properties");
        }
    }

    @Override
    public Object readObject(ByteBuf buf, Class<?> type) {
        validateType(type);
        Properties properties = new Properties();

        int size = buf.readInt();

        for (int index = 0; index < size; index++) {
            String key = ByteCodec.readString(buf);
            String val = ByteCodec.readString(buf);

            properties.put(key, val);
        }

        return properties;
    }

    @Override
    public void writeObject(ByteBuf buf, Object object) {
        validateType(object.getClass());

        Properties properties = ((Properties) object);
        Set<String> propertyNames = properties.stringPropertyNames();

        buf.writeInt(propertyNames.size());

        for (String name : propertyNames) {
            ByteCodec.writeString(buf, name);
            ByteCodec.writeString(buf, properties.getProperty(name));
        }
    }
}
