package me.moonways.bridgenet.mtp.transfer.provider;

import java.nio.ByteBuffer;
import java.util.Arrays;

import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.MessageBytes;
import org.mockito.internal.util.Primitives;

public class TransferPrimitiveProvider implements TransferProvider {

    private void validateAsPrimitive(Class<?> cls) {
        if (!Primitives.isPrimitiveOrWrapper(cls)) {
            throw new IllegalArgumentException("type");
        }
    }

    private boolean isBoolean(Class<?> cls) {
        return cls == Boolean.class || cls == Boolean.TYPE;
    }

    @Override
    public Object fromByteArray(ByteCodec byteCodec, Class<?> cls, MessageBytes messageBytes) {
        byte[] array = messageBytes.getArray();

        if (String.class.isAssignableFrom(cls)) {

            int length = byteCodec.readInt(Arrays.copyOfRange(array, 0, Integer.BYTES));
            messageBytes.moveTo(Integer.BYTES);
            messageBytes.moveTo(length);

            return new String(array, 0, length);
        }

        validateAsPrimitive(cls);

        if (isBoolean(cls)) {
            messageBytes.moveTo(1);
            return (array[0] == 1);
        }

        int bufferSize = byteCodec.toBufferSize(cls);
        messageBytes.moveTo(bufferSize);

        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

        byte[] src = Arrays.copyOfRange(array, 0, bufferSize);

        byteBuffer.put(src, 0, src.length);
        byteBuffer.flip();

        return byteCodec.read(cls, byteBuffer);
    }

    @Override
    public byte[] toByteArray(ByteCodec byteCodec, Object object) {
        final Class<?> cls = object.getClass();

        if (CharSequence.class.isAssignableFrom(cls)) {
            String value = object.toString();

            ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES + value.length());

            byteCodec.write(value.length(), byteBuffer);
            byteCodec.write(value, byteBuffer);

            return byteBuffer.array();
        }

        validateAsPrimitive(cls);

        if (isBoolean(cls)) {
            return new byte[]{(byte) ((Boolean) object ? 1 : 0)};
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(byteCodec.toBufferSize(cls));
        byteCodec.write(object, byteBuffer);

        byte[] array = byteBuffer.array();

        byteBuffer.clear();
        return array;
    }
}
