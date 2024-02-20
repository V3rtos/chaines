package me.moonways.bridgenet.mtp.transfer.provider;

import io.netty.buffer.ByteBuf;
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
    public Object readObject(ByteBuf buf, Class<?> type) {
        validateType(type);

        Object[] enumConstants = type.getEnumConstants();
        return enumConstants[buf.readInt()];
    }

    @Override
    public void writeObject(ByteBuf buf, Object object) {
        validateType(object.getClass());
        int ordinal = ((Enum<?>) object).ordinal();

        buf.writeInt(ordinal);
    }
}
