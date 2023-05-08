package me.moonways.bridgenet.transfer.provider;

import me.moonways.bridgenet.transfer.ByteCodec;
import me.moonways.bridgenet.transfer.MessageBytes;

public interface TransferProvider {

    Object provide(ByteCodec byteCodec, Class<?> cls, MessageBytes messageBytes);

    byte[] toByteArray(ByteCodec byteCodec, Object object);
}
