package me.moonways.bridgenet.mtp.transfer.provider;

import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.MessageBytes;

public interface TransferProvider {

    Object provide(ByteCodec byteCodec, Class<?> cls, MessageBytes messageBytes);

    byte[] toByteArray(ByteCodec byteCodec, Object object);
}
