package me.moonways.bridgenet.mtp.transfer.provider;

import io.netty.buffer.ByteBuf;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.MessageBytes;
import me.moonways.bridgenet.mtp.transfer.MessageTransferException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class TransferSerializeProvider implements TransferProvider {

    private void validateType(Class<?> cls) {
        if (!Serializable.class.isAssignableFrom(ByteCodec.getPrimitiveWrapper(cls))) {
            throw new IllegalArgumentException(cls + " not serializable");
        }
    }

    @Override
    public Object readObject(ByteBuf buf, Class<?> type) {
        validateType(type);

        int length = buf.readInt();
        byte[] bytes = ByteCodec.readBytesArray(length, buf);

        try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(stream)) {

            return objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException exception) {
            throw new MessageTransferException(exception);
        }
    }

    @Override
    public void writeObject(ByteBuf buf, Object object) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream)) {

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();

            byte[] serializedObjectArray = stream.toByteArray();

            buf.writeInt(serializedObjectArray.length);
            buf.writeBytes(serializedObjectArray);

        } catch (IOException exception) {
            throw new MessageTransferException(exception);
        }
    }
}
