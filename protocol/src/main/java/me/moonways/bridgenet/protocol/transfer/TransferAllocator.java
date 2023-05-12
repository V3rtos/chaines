package me.moonways.bridgenet.protocol.transfer;

import java.lang.reflect.Field;

import lombok.SneakyThrows;
import me.moonways.bridgenet.protocol.message.Message;
import sun.misc.Unsafe;

public class TransferAllocator {

    private static final Unsafe UNSAFE;

    static {
        try {
            Field unsafeInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeInstanceField.setAccessible(true);

            UNSAFE = ((Unsafe) unsafeInstanceField.get(null));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @SneakyThrows
    public <T extends Message> T allocatePacket(Class<?> cls, MessageTransfer messageTransfer) {
        Object packetObj = UNSAFE.allocateInstance(cls);

        @SuppressWarnings("unchecked") T packet = (T) packetObj;
        messageTransfer.unbuf(packet);
        return packet;
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <T> T allocate(Class<T> cls) {
        return (T) UNSAFE.allocateInstance(cls);
    }
}
