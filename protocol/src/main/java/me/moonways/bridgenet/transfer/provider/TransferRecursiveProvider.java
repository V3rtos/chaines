package me.moonways.bridgenet.transfer.provider;

import me.moonways.bridgenet.transfer.ByteCodec;
import me.moonways.bridgenet.transfer.MessageBytes;

public class TransferRecursiveProvider implements TransferProvider {

    @Override
    public Object provide(ByteCodec byteCodec, Class<?> cls, MessageBytes messageBytes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] toByteArray(ByteCodec byteCodec, Object object) {
        throw new UnsupportedOperationException();
    }
}
