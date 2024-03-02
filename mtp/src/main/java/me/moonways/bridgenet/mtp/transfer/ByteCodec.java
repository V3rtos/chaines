package me.moonways.bridgenet.mtp.transfer;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@UtilityClass
public final class ByteCodec {

    private static final int DEFAULT_PRIMITIVE_BUFFER_SIZE = 256;
    private static final int DEFAULT_OBJ_BUFFER_SIZE = 512;

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

    public boolean isPrimitiveOrWrapper(Class<?> cls) {
        Set<Class<?>> total = new HashSet<>();
        total.addAll(PRIMITIVE_TO_WRAPPER_MAP.keySet());
        total.addAll(PRIMITIVE_TO_WRAPPER_MAP.values());
        return total.contains(cls);
    }

    public int toBufferSize(Class<?> type) {
        if (isPrimitiveOrWrapper(type)) {
            try {
                Field bytesSizeField = getPrimitiveWrapper(type).getDeclaredField("BYTES");
                return bytesSizeField.getInt(null);

            } catch (Exception exception) {
                return DEFAULT_PRIMITIVE_BUFFER_SIZE;
            }
        }

        return DEFAULT_OBJ_BUFFER_SIZE;
    }

    public void writeString(ByteBuf buf, String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);

        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public String readString(ByteBuf buf) {
        int length = buf.readInt();
        byte[] arr = readBytesArray(length, buf);

        return new String(arr, StandardCharsets.UTF_8);
    }

    public byte[] readBytesArray(int length, ByteBuf byteBuf) {
        byte[] arr = new byte[length];

        byteBuf.readBytes(arr);
        return arr;
    }

    public byte[] readBytesArray(ByteBuf byteBuf) {
        return readBytesArray(byteBuf.readableBytes(), byteBuf);
    }
}
