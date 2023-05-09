package me.moonways.bridgenet.protocol.transfer.provider;

import me.moonways.bridgenet.protocol.transfer.ByteCodec;
import me.moonways.bridgenet.protocol.transfer.MessageBytes;

public interface TransferProvider {

    Object provide(ByteCodec byteCodec, Class<?> cls, MessageBytes messageBytes);

    byte[] toByteArray(ByteCodec byteCodec, Object object);
}
