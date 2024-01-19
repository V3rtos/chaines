package me.moonways.bridgenet.mtp.transfer.provider;

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
    public Object fromByteArray(ByteCodec byteCodec, Class<?> cls, MessageBytes messageBytes) {
        validateType(cls);

        long mostSigBits = byteCodec.readLong(Arrays.copyOfRange(messageBytes.getArray(), 0, Long.BYTES));
        messageBytes.moveTo(Long.BYTES);

        long leastSigBits = byteCodec.readLong(Arrays.copyOfRange(messageBytes.getArray(), 0, Long.BYTES));
        messageBytes.moveTo(Long.BYTES);

        return new UUID(mostSigBits, leastSigBits);
    }

    @Override
    public byte[] toByteArray(ByteCodec byteCodec, Object object) {
        validateType(object.getClass());

        UUID uuid = (UUID) object;

        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();

        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES * 2);
        byteCodec.write(mostSignificantBits, byteBuffer);
        byteCodec.write(leastSignificantBits, byteBuffer);

        return byteBuffer.array();
    }
}
