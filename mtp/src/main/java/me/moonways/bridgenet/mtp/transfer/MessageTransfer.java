package me.moonways.bridgenet.mtp.transfer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactory;
import me.moonways.bridgenet.api.inject.bean.factory.type.ConstructorFactory;
import me.moonways.bridgenet.api.inject.bean.factory.type.UnsafeFactory;
import me.moonways.bridgenet.api.util.reflection.ReflectionUtils;
import me.moonways.bridgenet.mtp.transfer.provider.TransferProvider;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageTransfer {

    private static final BeanFactory OBJECT_FACTORY = new ConstructorFactory();

    public static MessageTransfer decode(ByteBuf byteBuf) {
        return new MessageTransfer(null, byteBuf);
    }

    public static MessageTransfer encode(Object message) {
        return new MessageTransfer(message, null);
    }

    private Object messagePacket;

    @Getter
    private ByteBuf byteBuf;

    private void validatePacket() {
        if (messagePacket == null) {
            throw new IllegalArgumentException("packet");
        }
        if (byteBuf == null) {
            byteBuf = Unpooled.buffer();
        }
    }

    public void buf() {
        validatePacket();
        reflectiveBuf();
    }

    private Class<? extends TransferProvider> getFieldProvider(Field field) {
        ByteTransfer declaredAnnotation = field.getDeclaredAnnotation(ByteTransfer.class);

        if (declaredAnnotation == null) {
            log.warn("§6Message field '{}' is not transferable then to be ignored", field);
            return null;
        }

        Class<? extends TransferProvider> provider = declaredAnnotation.provider();
        if (provider == null)
            log.warn("§6Provider type for " + field + " is not initialized");

        return provider;
    }

    private void reflectiveBuf() {
        Class<?> packetType = messagePacket.getClass();
        Field[] declaredFieldsArray = packetType.getDeclaredFields();

        for (Field field : declaredFieldsArray) {
            Class<? extends TransferProvider> provider = getFieldProvider(field);

            TransferProvider transferProvider = OBJECT_FACTORY.create(provider);

            try {
                ReflectionUtils.grantAccess(field);

                Object value = field.get(messagePacket);
                if (Iterable.class.isAssignableFrom(field.getType())) {
                    bufIterableField((Iterable) value, transferProvider, byteBuf);
                } else {
                    bufField(value, transferProvider, byteBuf);
                }
            } catch (Throwable exception) {
                throw new MessageTransferException(exception);
            }
        }
    }

    public void unbuf(Object message) {
        this.messagePacket = message;
        reflectiveUnbuf();
    }

    private void reflectiveUnbuf() {
        Class<?> packetType = messagePacket.getClass();
        Field[] declaredFieldsArray = packetType.getDeclaredFields();

        for (Field field : declaredFieldsArray) {
            Class<? extends TransferProvider> provider = getFieldProvider(field);

            TransferProvider transferProvider = OBJECT_FACTORY.create(provider);

            if (List.class.isAssignableFrom(field.getType())) {
                unbufIterableField(new ArrayList(), field, transferProvider, byteBuf);
            } else if (Set.class.isAssignableFrom(field.getType())) {
                unbufIterableField(new HashSet(), field, transferProvider, byteBuf);
            } else {
                unbufField(field, transferProvider, byteBuf);
            }
        }
    }

    private void unbufIterableField(Collection collection, Field field, TransferProvider provider, ByteBuf byteBuf) {
        try {
            Class<?> genericType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            int size = byteBuf.readInt();

            for (int i = 0; i < size; i++) {
                collection.add(provider.readObject(byteBuf, genericType));
            }

            ReflectionUtils.grantAccess(field);
            field.set(messagePacket, collection);
        } catch (Throwable exception) {
            throw new MessageTransferException(exception);
        }
    }

    private void unbufField(Field field, TransferProvider provider, ByteBuf byteBuf) {
        Object providedObject = provider.readObject(byteBuf, field.getType());

        try {
            ReflectionUtils.grantAccess(field);
            field.set(messagePacket, providedObject);
        } catch (Throwable exception) {
            throw new MessageTransferException(exception);
        }
    }

    private void bufIterableField(Iterable iterable, TransferProvider provider, ByteBuf byteBuf) {
        int size = (int) iterable.spliterator().estimateSize();
        byteBuf.writeInt(size);

        for (Object object : iterable) {
            provider.writeObject(byteBuf, object);
        }
    }

    private void bufField(Object fieldValue, TransferProvider provider, ByteBuf byteBuf) {
        provider.writeObject(byteBuf, fieldValue);
    }
}
