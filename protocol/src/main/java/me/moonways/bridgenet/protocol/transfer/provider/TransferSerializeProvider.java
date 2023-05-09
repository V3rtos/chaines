package me.moonways.bridgenet.protocol.transfer.provider;

import me.moonways.bridgenet.protocol.transfer.ByteCodec;
import me.moonways.bridgenet.protocol.transfer.MessageBytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class TransferSerializeProvider implements TransferProvider {

    private void validateType(ByteCodec byteCodec, Class<?> cls) {
        if (!Serializable.class.isAssignableFrom(byteCodec.getPrimitiveWrapper(cls))) {
            throw new IllegalArgumentException(cls + " not serializable");
        }
    }

    @Override
    public Object provide(ByteCodec byteCodec, Class<?> cls, MessageBytes messageBytes) {
        validateType(byteCodec, cls);

        int bytesLength = byteCodec.readInt(Arrays.copyOfRange(messageBytes.getArray(), 0, Integer.BYTES));
        messageBytes.addPosition(Integer.BYTES);

        try (ByteArrayInputStream stream = new ByteArrayInputStream(messageBytes.getArray(), 0, bytesLength);
             ObjectInputStream objectInputStream = new ObjectInputStream(stream)) {

            return objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException exception) {
            throw new RuntimeException(exception);
        } finally {
            messageBytes.addPosition(bytesLength);
        }
    }

    @Override
    public byte[] toByteArray(ByteCodec byteCodec, Object object) {
        validateType(byteCodec, object.getClass());

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream)) {

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();

            byte[] serializedObjectArray = stream.toByteArray();

            byte[] startSizeIndexAsBytesArray = byteCodec.toByteArray(serializedObjectArray.length);
            byte[] resultArray = new byte[serializedObjectArray.length + startSizeIndexAsBytesArray.length];

            System.arraycopy(startSizeIndexAsBytesArray, 0, resultArray, 0, startSizeIndexAsBytesArray.length);
            System.arraycopy(serializedObjectArray, 0, resultArray, startSizeIndexAsBytesArray.length, serializedObjectArray.length);

            return resultArray;

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
