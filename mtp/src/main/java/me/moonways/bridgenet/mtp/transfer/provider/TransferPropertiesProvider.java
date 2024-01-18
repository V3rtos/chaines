package me.moonways.bridgenet.mtp.transfer.provider;

import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.MessageBytes;

import java.nio.ByteBuffer;
import java.util.*;

public class TransferPropertiesProvider implements TransferProvider {

    private void validateType(Class<?> cls) {
        if (!Properties.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException(cls + " is not assignable from java.util.Properties");
        }
    }

    @Override
    public Object fromByteArray(ByteCodec byteCodec, Class<?> cls, MessageBytes messageBytes) {
        validateType(cls);

        int propertiesSize = byteCodec.readInt(Arrays.copyOfRange(messageBytes.getArray(), 0, Integer.BYTES));
        messageBytes.moveTo(Integer.BYTES);

        Properties properties = new Properties();

        for (int index = 0; index < propertiesSize; index++) {
            int keyLength = byteCodec.readInt(Arrays.copyOfRange(messageBytes.getArray(), 0, Integer.BYTES));
            messageBytes.moveTo(Integer.BYTES);

            int valueLength = byteCodec.readInt(Arrays.copyOfRange(messageBytes.getArray(), 0, Integer.BYTES));
            messageBytes.moveTo(Integer.BYTES);

            String key = byteCodec.readString(Arrays.copyOfRange(messageBytes.getArray(), 0, keyLength));
            messageBytes.moveTo(keyLength);

            String value = byteCodec.readString(Arrays.copyOfRange(messageBytes.getArray(), 0, valueLength));
            messageBytes.moveTo(valueLength);

            properties.put(key, value);
        }

        return properties;
    }

    @Override
    public byte[] toByteArray(ByteCodec byteCodec, Object object) {
        validateType(object.getClass());

        Properties properties = ((Properties) object);
        Set<String> propertyNames = properties.stringPropertyNames();

        int stringsTotalCapacity = propertyNames.stream().mapToInt(n -> Integer.BYTES * 2 + (properties.getProperty(n) + n).length()).sum();

        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES + stringsTotalCapacity);
        byteCodec.write(propertyNames.size(), byteBuffer);

        for (String key : propertyNames) {
            String value = properties.getProperty(key);

            byteCodec.write(key.length(), byteBuffer);
            byteCodec.write(value.length(), byteBuffer);
            byteCodec.write(key, byteBuffer);
            byteCodec.write(value, byteBuffer);
        }

        return byteBuffer.array();
    }
}
