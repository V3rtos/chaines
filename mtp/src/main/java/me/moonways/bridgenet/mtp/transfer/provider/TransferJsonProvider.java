package me.moonways.bridgenet.mtp.transfer.provider;

import com.google.gson.Gson;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.MessageBytes;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class TransferJsonProvider implements TransferProvider {

    private final Gson gson = new Gson();

    @Override
    public Object fromByteArray(ByteCodec byteCodec, Class<?> cls, MessageBytes messageBytes) {
        int bytesLength = byteCodec.readInt(Arrays.copyOfRange(messageBytes.getArray(), 0, Integer.BYTES));
        messageBytes.moveTo(Integer.BYTES);

        byte[] bytes = Arrays.copyOfRange(messageBytes.getArray(), 0, bytesLength);
        messageBytes.moveTo(bytesLength);

        return gson.fromJson(new String(bytes), cls);
    }

    @Override
    public byte[] toByteArray(ByteCodec byteCodec, Object object) {
        String json = gson.toJson(object);

        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES + json.length());

        byteCodec.write(json.length(), byteBuffer);
        byteCodec.write(json, byteBuffer);

        return byteBuffer.array();
    }
}
