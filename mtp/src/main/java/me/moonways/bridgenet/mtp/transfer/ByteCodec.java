package me.moonways.bridgenet.mtp.transfer;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.mockito.internal.util.Primitives;

public final class ByteCodec {

    private static final int DEFAULT_PRIMITIVE_BUFFER_SIZE = 256;
    private static final int DEFAULT_OBJ_BUFFER_SIZE = 512;

    public static final ByteBuffer INT_BUFFER = ByteBuffer.allocate(Integer.BYTES);
    public static final ByteBuffer LONG_BUFFER = ByteBuffer.allocate(Long.BYTES);

    private final static Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_MAP = new HashMap<>();

    static {
        PRIMITIVE_TO_WRAPPER_MAP.put(boolean.class, Boolean.class);
        PRIMITIVE_TO_WRAPPER_MAP.put(byte.class, Byte.class);
        PRIMITIVE_TO_WRAPPER_MAP.put(short.class, Short.class);
        PRIMITIVE_TO_WRAPPER_MAP.put(char.class, Character.class);
        PRIMITIVE_TO_WRAPPER_MAP.put(int.class, Integer.class);
        PRIMITIVE_TO_WRAPPER_MAP.put(long.class, Long.class);
        PRIMITIVE_TO_WRAPPER_MAP.put(float.class, Float.class);
        PRIMITIVE_TO_WRAPPER_MAP.put(double.class, Double.class);
    }

    public Class<?> getPrimitiveWrapper(Class<?> cls) {
        return PRIMITIVE_TO_WRAPPER_MAP.getOrDefault(cls, cls);
    }

    public int toBufferSize(Class<?> type) {
        if (Primitives.isPrimitiveOrWrapper(type)) {
            try {
                Field bytesSizeField = getPrimitiveWrapper(type).getDeclaredField("BYTES");
                return bytesSizeField.getInt(null);

            } catch (Exception exception) {
                return DEFAULT_PRIMITIVE_BUFFER_SIZE;
            }
        }

        return DEFAULT_OBJ_BUFFER_SIZE;
    }

    public Object read(Class<?> cls, ByteBuffer byteBuffer) {
        if (byte.class.equals(cls) || Byte.class.equals(cls)) {
            return byteBuffer.get();
        }

        if (short.class.equals(cls) || Short.class.equals(cls)) {
            return byteBuffer.getShort();
        }

        if (int.class.equals(cls) || Integer.class.equals(cls)) {
            return byteBuffer.getInt();
        }

        if (double.class.equals(cls) || Double.class.equals(cls)) {
            return byteBuffer.getDouble();
        }

        if (float.class.equals(cls) || Float.class.equals(cls)) {
            return byteBuffer.getFloat();
        }

        if (long.class.equals(cls) || Long.class.equals(cls)) {
            return byteBuffer.getLong();
        }

        if (char.class.equals(cls) || Character.class.equals(cls)) {
            return byteBuffer.getChar();
        }

        if (String.class.equals(cls)) {
            return readString(byteBuffer.array());
        }

        return Primitives.defaultValueForPrimitiveOrWrapper(cls);
    }

    public void write(Object obj, ByteBuffer byteBuffer) {
        if (obj instanceof byte[]) {
            byteBuffer.put((byte[]) obj);
        }

        if (obj instanceof Byte) {
            byteBuffer.put((Byte) obj);
        }

        if (obj instanceof Short) {
            byteBuffer.putShort((Short) obj);
        }

        if (obj instanceof Integer) {
            byteBuffer.putInt((Integer) obj);
        }

        if (obj instanceof Double) {
            byteBuffer.putDouble((Double) obj);
        }

        if (obj instanceof Float) {
            byteBuffer.putFloat((Float) obj);
        }

        if (obj instanceof Long) {
            byteBuffer.putLong((Long) obj);
        }

        if (obj instanceof Character) {
            byteBuffer.putChar((Character) obj);
        }

        if (obj instanceof String) {
            byteBuffer.put(obj.toString().getBytes());
        }
    }

    public byte[] toByteArray(int value) {
        INT_BUFFER.putInt(value);

        byte[] array = INT_BUFFER.array();
        INT_BUFFER.clear();

        return array;
    }

    public byte[] toByteArray(long value) {
        LONG_BUFFER.putLong(value);

        byte[] array = LONG_BUFFER.array();
        LONG_BUFFER.clear();

        return array;
    }

    public int readInt(byte[] array) {
        INT_BUFFER.put(array, 0, array.length);
        ((Buffer)INT_BUFFER).flip(); // так надо, я не знаю почему, иначе выдает NoSuchMethod

        int value = INT_BUFFER.getInt();
        ((Buffer)INT_BUFFER).clear(); // так надо, я не знаю почему, иначе выдает NoSuchMethod

        return value;
    }

    public long readLong(byte[] array) {
        LONG_BUFFER.put(array, 0, array.length);
        ((Buffer)LONG_BUFFER).flip(); // так надо, я не знаю почему, иначе выдает NoSuchMethod

        long value = LONG_BUFFER.getLong();
        ((Buffer)LONG_BUFFER).clear(); // так надо, я не знаю почему, иначе выдает NoSuchMethod

        return value;
    }

    public String readString(byte[] bytes, Charset charset) {
        return new String(bytes, 0, bytes.length, charset);
    }

    public String readString(byte[] bytes) {
        return readString(bytes, Charset.defaultCharset());
    }
}
