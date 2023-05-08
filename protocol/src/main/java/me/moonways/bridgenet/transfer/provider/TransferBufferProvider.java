package me.moonways.bridgenet.transfer.provider;

import java.nio.ByteBuffer;
import java.util.Arrays;

import me.moonways.bridgenet.transfer.ByteCodec;
import me.moonways.bridgenet.transfer.MessageBytes;
import org.mockito.internal.util.Primitives;

public class TransferBufferProvider implements TransferProvider {

    private void validateAsPrimitive(Class<?> cls) {
        if (!Primitives.isPrimitiveOrWrapper(cls)) {
            throw new IllegalArgumentException("type");
        }
    }

    private boolean isBoolean(Class<?> cls) {
        return cls == Boolean.class || cls == Boolean.TYPE;
    }

    @Override
    public Object provide(ByteCodec byteCodec, Class<?> cls, MessageBytes messageBytes) {
        byte[] array = messageBytes.getArray();

        if (array.length == 0) {
            return null;
        }

        validateAsPrimitive(cls);

        if (isBoolean(cls)) {
            return (array[0] == 1);
        }

        int bufferSize = byteCodec.toBufferSize(cls);
        messageBytes.addPosition(bufferSize);

        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

        byte[] src = Arrays.copyOfRange(array, 0, bufferSize);

        byteBuffer.put(src, 0, src.length);
        byteBuffer.flip();

        return byteCodec.read(cls, byteBuffer);
    }

    @Override
    public byte[] toByteArray(ByteCodec byteCodec, Object object) {
        final Class<?> cls = object.getClass();
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
