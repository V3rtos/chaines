package me.moonways.bridgenet.mtp.transfer.provider;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;

public class ToJsonProvider implements TransferProvider {

    private final Gson gson = new Gson();

    @Override
    public Object readObject(ByteBuf buf, Class<?> type) {
        String json = ByteCodec.readString(buf);
        return gson.fromJson(json, type);
    }

    @Override
    public void writeObject(ByteBuf buf, Object object) {
        String json = gson.toJson(object);
        ByteCodec.writeString(buf, json);
    }
}
