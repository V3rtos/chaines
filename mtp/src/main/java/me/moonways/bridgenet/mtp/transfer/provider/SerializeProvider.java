package me.moonways.bridgenet.mtp.transfer.provider;

import io.netty.buffer.ByteBuf;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.MessageTransferException;

import java.io.*;

public class SerializeProvider implements TransferProvider {

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
