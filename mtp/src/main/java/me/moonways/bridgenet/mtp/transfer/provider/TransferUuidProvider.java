package me.moonways.bridgenet.mtp.transfer.provider;

import io.netty.buffer.ByteBuf;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.MessageBytes;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

public class TransferUuidProvider implements TransferProvider {

    private void validateType(Class<?> cls) {
        if (!UUID.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException(cls + " is not assignable from java.util.UUID");
        }
    }

    @Override
    public Object readObject(ByteBuf buf, Class<?> type) {
        validateType(type);
        return new UUID(buf.readLong(), buf.readLong());
    }

    @Override
    public void writeObject(ByteBuf buf, Object object) {
        validateType(object.getClass());

        UUID uuid = (UUID) object;

        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }
}
