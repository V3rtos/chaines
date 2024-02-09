package me.moonways.bridgenet.mtp.transfer;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactory;
import me.moonways.bridgenet.api.inject.bean.factory.UnsafeFactory;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessorAdapter;
import me.moonways.bridgenet.mtp.transfer.provider.TransferProvider;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageTransfer {

    private static final ByteCodec BYTE_CODEC = new ByteCodec();
    private static final BeanFactory OBJECT_FACTORY = new UnsafeFactory();

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

        if (bytes.length > 0) {
            reflectiveBuf();
        }
    }

    private Class<? extends TransferProvider> getFieldProvider(Field field) {
        ByteTransfer declaredAnnotation = field.getDeclaredAnnotation(ByteTransfer.class);

        if (declaredAnnotation == null) {
            log.warn("Message field '{}' is not transferable then to be ignored", field);
            return null;
        }

        Class<? extends TransferProvider> provider = declaredAnnotation.provider();
        if (provider == null)
            throw new MessageTransferException("Provider for " + field + " is not initialized");

        return provider;
    }

    private void reflectiveBuf() {
        Class<?> packetType = messagePacket.getClass();
        Field[] declaredFieldsArray = packetType.getDeclaredFields();

        int lastIndex = 0;

        for (Field field : declaredFieldsArray) {
            Class<? extends TransferProvider> provider = getFieldProvider(field);

            TransferProvider transferProvider = OBJECT_FACTORY.create(provider);

            byte[] bytesArray;
            try {
                field.setAccessible(true);

                Object value = field.get(messagePacket);
                if (Iterable.class.isAssignableFrom(field.getType())) {
                    bytesArray = bufIterableField((Iterable) value, transferProvider);
                } else {
                    bytesArray = bufField(value, transferProvider);
                }
            }
            catch (IllegalAccessException exception) {
                throw new MessageTransferException(exception);
            }

            System.arraycopy(bytesArray, 0, bytes, lastIndex, bytesArray.length);
            lastIndex += bytesArray.length;
        }

        if (lastIndex < bytes.length)
            bytes = Arrays.copyOfRange(bytes, 0, lastIndex);
    }

    public void unbuf(Object message) {
        //validateMessage(0);
        this.messagePacket = message;

        if (bytes.length > 0) {
            reflectiveUnbuf();
        }
    }

    private void reflectiveUnbuf() {
        Class<?> packetType = messagePacket.getClass();
        Field[] declaredFieldsArray = packetType.getDeclaredFields();

        MessageBytes messageBytes = MessageBytes.create(bytes);

        for (Field field : declaredFieldsArray) {
            Class<? extends TransferProvider> provider = getFieldProvider(field);

            TransferProvider transferProvider = OBJECT_FACTORY.create(provider);

            if (List.class.isAssignableFrom(field.getType())) {
                unbufIterableField(new ArrayList(), field, transferProvider, messageBytes);
            } else if (Set.class.isAssignableFrom(field.getType())) {
                unbufIterableField(new HashSet(), field, transferProvider, messageBytes);
            } else {
                unbufField(field, transferProvider, messageBytes);
            }
        }
    }

    private void unbufIterableField(Collection collection, Field field, TransferProvider provider, MessageBytes messageBytes) {
        try {
            Class<?> genericType = null;

            int size = BYTE_CODEC.readInt(Arrays.copyOfRange(messageBytes.getArray(), 0, Integer.BYTES));
            messageBytes.moveTo(Integer.BYTES);

            // todo - в будущем нужно будет переделать на получение класса дженерика у листа (класс листа лежит в field.getType()), обычные методы не помогают - выдают null
            if (size > 0) {
                int nameLength = BYTE_CODEC.readInt(Arrays.copyOfRange(messageBytes.getArray(), 0, Integer.BYTES));
                messageBytes.moveTo(Integer.BYTES);

                String classname = BYTE_CODEC.readString(Arrays.copyOfRange(messageBytes.getArray(), 0, nameLength), StandardCharsets.UTF_16LE);
                messageBytes.moveTo(nameLength);

                genericType = Class.forName(classname);
            }

            for (int i = 0; i < size; i++) {
                collection.add(provider.fromByteArray(BYTE_CODEC, genericType, messageBytes));
            }

            field.setAccessible(true);
            field.set(messagePacket, collection);
        }
        catch (Exception exception) {
            throw new MessageTransferException(exception);
        }
    }

    private void unbufField(Field field, TransferProvider provider, MessageBytes messageBytes) {
        Object providedObject = provider.fromByteArray(BYTE_CODEC, field.getType(), messageBytes);

        try {
            field.setAccessible(true);
            field.set(messagePacket, providedObject);
        }
        catch (IllegalAccessException exception) {
            throw new MessageTransferException(exception);
        }
    }

    private byte[] bufIterableField(Iterable iterable, TransferProvider provider) {
        List<Byte> bytes = new ArrayList<>();

        int size = (int) iterable.spliterator().estimateSize();
        for (byte b : BYTE_CODEC.toByteArray(size)) {
            bytes.add(b);
        }

        // todo - в будущем нужно будет переделать на получение класса дженерика у листа (класс листа лежит в field.getType()), обычные методы не помогают - выдают null
        if (size > 0) {
            Object first = iterable.iterator().next();
            String classname = first.getClass().getName();

            byte[] stringBytes = classname.getBytes(StandardCharsets.UTF_16LE);

            for (byte b : BYTE_CODEC.toByteArray(stringBytes.length)) {
                bytes.add(b);
            }
            for (byte b : stringBytes) {
                bytes.add(b);
            }
        }

        Iterator iterator = iterable.iterator();
        while (iterator.hasNext()) {
            for (byte b : bufField(iterator.next(), provider)) {
                bytes.add(b);
            }
        }

        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i);
        }

        return result;
    }

    private byte[] bufField(Object fieldValue, TransferProvider provider) {
        return provider.toByteArray(BYTE_CODEC, fieldValue);
    }
}
