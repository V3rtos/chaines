package me.moonways.bridgenet.mtp.transfer;

import java.lang.reflect.Field;
import java.util.Arrays;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.moonways.bridgenet.api.inject.factory.UnsafeObjectFactory;
import me.moonways.bridgenet.mtp.transfer.provider.TransferProvider;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageTransfer {

    private static final ByteCodec BYTE_CODEC = new ByteCodec();

    public static MessageTransfer decode(byte[] bytes) {
        return new MessageTransfer(null, bytes);
    }

    public static MessageTransfer encode(Object message) {
        return new MessageTransfer(message, null);
    }

    private Object messagePacket;

    @Getter
    private byte[] bytes;

    private void validatePacket() {
        if (messagePacket == null) {
            throw new IllegalArgumentException("packet");
        }
    }

    private void validateMessage(int minSize) {
        if (bytes == null) {
            throw new IllegalArgumentException("message");
        }

        if (bytes.length < minSize) {
            throw new IllegalArgumentException("message size must be >= " + minSize);
        }
    }

    private void ensureBuffers() {
        if (bytes == null)
            bytes = new byte[0];

        int collectedSize = collectSize();
        bytes = Arrays.copyOfRange(bytes, 0, collectedSize);
    }

    private int collectSize() {
        validatePacket();
        return reflectiveSizeCollect();
    }

    private int reflectiveSizeCollect() {
        final Class<?> packetType = messagePacket.getClass();
        int size = 0;

        Field[] declaredFieldsArray = packetType.getDeclaredFields();

        for (Field field : declaredFieldsArray) {
            if (!field.isAnnotationPresent(ByteTransfer.class))
                continue;

            Class<?> type = field.getType();
            size += BYTE_CODEC.toBufferSize(type);
        }

        return size;
    }

    public void buf() {
        validatePacket();

        ensureBuffers();
        reflectiveBuf();
    }

    private void reflectiveBuf() {
        Class<?> packetType = messagePacket.getClass();
        Field[] declaredFieldsArray = packetType.getDeclaredFields();

        int lastIndex = 0;

        for (Field field : declaredFieldsArray) {
            ByteTransfer declaredAnnotation = field.getDeclaredAnnotation(ByteTransfer.class);
            if (declaredAnnotation == null)
                continue;

            Class<? extends TransferProvider> provider = declaredAnnotation.provider();
            if (provider == null)
                continue;

            field.setAccessible(true);

            TransferProvider transferProvider = new UnsafeObjectFactory()
                    .create(provider);

            byte[] bytesArray;

            try {
                bytesArray = transferProvider.toByteArray(BYTE_CODEC, field.get(messagePacket));
            }
            catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }

            System.arraycopy(bytesArray, 0, bytes, lastIndex, bytesArray.length);
            lastIndex += bytesArray.length;
        }

        if (lastIndex < bytes.length)
            bytes = Arrays.copyOfRange(bytes, 0, lastIndex);
    }

    public void unbuf(Object message) {
        validateMessage(1);
        this.messagePacket = message;

        reflectiveUnbuf();
    }

    private void reflectiveUnbuf() {
        Class<?> packetType = messagePacket.getClass();
        Field[] declaredFieldsArray = packetType.getDeclaredFields();

        MessageBytes messageBytes = MessageBytes.create(bytes);

        for (Field field : declaredFieldsArray) {
            ByteTransfer declaredAnnotation = field.getDeclaredAnnotation(ByteTransfer.class);
            if (declaredAnnotation == null)
                continue;

            Class<? extends TransferProvider> provider = declaredAnnotation.provider();

            if (provider == null)
                continue;

            TransferProvider transferProvider = new UnsafeObjectFactory()
                    .create(provider);

            Object providedObject = transferProvider.provide(BYTE_CODEC, field.getType(), messageBytes);

            field.setAccessible(true);

            try {
                field.set(messagePacket, providedObject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
