package me.moonways.bridgenet.mtp.transfer.provider;

import io.netty.buffer.ByteBuf;

public class ToEnumProvider implements TransferProvider {

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
