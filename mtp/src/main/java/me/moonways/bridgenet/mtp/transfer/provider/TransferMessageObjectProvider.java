package me.moonways.bridgenet.mtp.transfer.provider;

import me.moonways.bridgenet.api.inject.bean.factory.BeanFactory;
import me.moonways.bridgenet.api.inject.bean.factory.ConstructorFactory;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.MessageBytes;
import me.moonways.bridgenet.mtp.transfer.MessageTransfer;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class TransferMessageObjectProvider implements TransferProvider {

    private static final BeanFactory OBJECT_FACTORY = new ConstructorFactory();

    @Override
    public Object fromByteArray(ByteCodec byteCodec, Class<?> cls, MessageBytes messageBytes) {
        int bytesLength = byteCodec.readInt(Arrays.copyOfRange(messageBytes.getArray(), 0, Integer.BYTES));
        messageBytes.moveTo(Integer.BYTES);

        byte[] bytes = Arrays.copyOfRange(messageBytes.getArray(), 0, bytesLength);
        messageBytes.moveTo(bytesLength);

        Object message = OBJECT_FACTORY.create(cls);

        MessageTransfer messageTransfer = MessageTransfer.decode(bytes);
        messageTransfer.unbuf(message);

        return message;
    }

    @Override
    public byte[] toByteArray(ByteCodec byteCodec, Object object) {
        MessageTransfer messageTransfer = MessageTransfer.encode(object);
        messageTransfer.buf();

        byte[] messageBytes = messageTransfer.getBytes();

        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES + messageBytes.length);

        byteCodec.write(messageBytes.length, byteBuffer);
        byteCodec.write(messageBytes, byteBuffer);

        return byteBuffer.array();
    }
}
