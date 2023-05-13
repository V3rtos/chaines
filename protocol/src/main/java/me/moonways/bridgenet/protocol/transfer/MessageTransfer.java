package me.moonways.bridgenet.protocol.transfer;

import java.lang.reflect.Field;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.transfer.provider.TransferProvider;

@AllArgsConstructor
public final class MessageTransfer {

    private static final ByteCodec BYTE_CODEC = new ByteCodec();

    private Message messageObject;

    @Getter
    private byte[] bytes;

    private final TransferAllocator transferAllocator = new TransferAllocator();

    private void validatePacket() {
        if (messageObject == null) {
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
        final Class<? extends Message> packetType = messageObject.getClass();
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
        Class<? extends Message> packetType = messageObject.getClass();
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

            TransferProvider transferProvider = transferAllocator.allocate(provider);

            byte[] bytesArray;

            try {
                bytesArray = transferProvider.toByteArray(BYTE_CODEC, field.get(messageObject));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            System.arraycopy(bytesArray, 0, bytes, lastIndex, bytesArray.length);
            lastIndex += bytesArray.length;
        }

        if (lastIndex < bytes.length)
            bytes = Arrays.copyOfRange(bytes, 0, lastIndex);
    }

    public void unbuf(Message message) {
        validateMessage(1);
        this.messageObject = message;

        reflectiveUnbuf();
    }

    private void reflectiveUnbuf() {
        Class<? extends Message> packetType = messageObject.getClass();
        Field[] declaredFieldsArray = packetType.getDeclaredFields();

        MessageBytes messageBytes = MessageBytes.create(bytes);

        for (Field field : declaredFieldsArray) {
            ByteTransfer declaredAnnotation = field.getDeclaredAnnotation(ByteTransfer.class);
            if (declaredAnnotation == null)
                continue;

            Class<? extends TransferProvider> provider = declaredAnnotation.provider();

            if (provider == null)
                continue;

            TransferProvider transferProvider = transferAllocator.allocate(provider);

            Object providedObject = transferProvider.provide(BYTE_CODEC, field.getType(), messageBytes);

            field.setAccessible(true);

            try {
                field.set(messageObject, providedObject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
