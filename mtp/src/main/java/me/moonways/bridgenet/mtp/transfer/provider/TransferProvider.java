package me.moonways.bridgenet.mtp.transfer.provider;

import io.netty.buffer.ByteBuf;

public interface TransferProvider {

    Object readObject(ByteBuf buf, Class<?> type);

    void writeObject(ByteBuf buf, Object object);
}
