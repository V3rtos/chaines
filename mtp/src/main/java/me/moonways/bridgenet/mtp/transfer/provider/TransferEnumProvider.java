package me.moonways.bridgenet.mtp.transfer.provider;

import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.MessageBytes;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class TransferEnumProvider implements TransferProvider {

    private void validateType(Class<?> cls) {
        if (!Enum.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException(cls + " is not assignable from java.util.Enum");
        }
    }

    @Override
    public Object fromByteArray(ByteCodec byteCodec, Class<?> cls, MessageBytes messageBytes) {
        validateType(cls);

        int ordinal = byteCodec.readInt(Arrays.copyOfRange(messageBytes.getArray(), 0, Integer.BYTES));
        messageBytes.moveTo(Integer.BYTES);

        Object[] enumConstants = cls.getEnumConstants();

        return enumConstants[ordinal];
    }

    @Override
    public byte[] toByteArray(ByteCodec byteCodec, Object object) {
        validateType(object.getClass());

        int ordinal = ((Enum<?>) object).ordinal();

        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
        byteCodec.write(ordinal, byteBuffer);

        return byteBuffer.array();
    }
}
