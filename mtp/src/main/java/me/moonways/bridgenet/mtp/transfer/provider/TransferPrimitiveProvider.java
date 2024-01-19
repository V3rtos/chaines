package me.moonways.bridgenet.mtp.transfer.provider;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;

import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.MessageBytes;

public class TransferPrimitiveProvider implements TransferProvider {

    private void validateAsPrimitive(ByteCodec byteCodec, Class<?> cls) {
        if (!byteCodec.isPrimitiveOrWrapper(cls)) {
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

            String value = new String(Arrays.copyOfRange(array, Integer.BYTES, Integer.BYTES + length));
            messageBytes.moveTo(length);

            return value;
        }

        validateAsPrimitive(byteCodec, cls);

        if (isBoolean(cls)) {
            messageBytes.moveTo(1);
            return (array[0] == 1);
        }

        int bufferSize = byteCodec.toBufferSize(cls);
        messageBytes.moveTo(bufferSize);

        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

        byte[] src = Arrays.copyOfRange(array, 0, bufferSize);

        byteBuffer.put(src, 0, src.length);
        ((Buffer) byteBuffer).flip();

        return byteCodec.read(cls, byteBuffer);
    }

    @Override
    public byte[] toByteArray(ByteCodec byteCodec, Object object) {
        final Class<?> cls = object.getClass();

        if (String.class.isAssignableFrom(cls)) {
            byte[] bytes = ((String) object).getBytes();

            ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES + bytes.length);

            byteCodec.write(bytes.length, byteBuffer);
            byteCodec.write(bytes, byteBuffer);

            return byteBuffer.array();
        }

        validateAsPrimitive(byteCodec, cls);

        if (isBoolean(cls)) {
            return new byte[]{(byte) ((Boolean) object ? 1 : 0)};
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(byteCodec.toBufferSize(cls));
        byteCodec.write(object, byteBuffer);

        byte[] array = byteBuffer.array();

        ((Buffer) byteBuffer).clear();
        return array;
    }
}
