package me.moonways.bridgenet.mtp.transfer.provider;

import io.netty.buffer.ByteBuf;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.MessageTransferException;

public class DefaultProvider implements TransferProvider {

    private void validateAsPrimitive(Class<?> cls) {
        if (!ByteCodec.isPrimitiveOrWrapper(cls)) {
            throw new IllegalArgumentException("type");
        }
    }

    private boolean isBoolean(Class<?> cls) {
        return cls == Boolean.class || cls == Boolean.TYPE;
    }

    private boolean isAssignableFrom(Class<?> primitiveType, Class<?> target) {
        Class<?> wrapper = ByteCodec.getPrimitiveWrapper(primitiveType);
        return primitiveType.equals(target) || wrapper.isAssignableFrom(target) || target.isAssignableFrom(wrapper);
    }

    @Override
    public Object readObject(ByteBuf buf, Class<?> type) {
        if (String.class.isAssignableFrom(type)) {
            return ByteCodec.readString(buf);
        }

        validateAsPrimitive(type);
        if (isBoolean(type)) {
            return buf.readBoolean();
        }
        if (isAssignableFrom(int.class, type)) {
            return buf.readInt();
        }
        if (isAssignableFrom(long.class, type)) {
            return buf.readLong();
        }
        if (isAssignableFrom(double.class, type)) {
            return buf.readDouble();
        }
        if (isAssignableFrom(float.class, type)) {
            return buf.readFloat();
        }
        if (isAssignableFrom(byte.class, type)) {
            return buf.readByte();
        }
        if (isAssignableFrom(short.class, type)) {
            return buf.readShort();
        }
        throw new MessageTransferException("not primitive");
    }

    @Override
    public void writeObject(ByteBuf buf, Object object) {
        Class<?> type = object.getClass();

        if (String.class.isAssignableFrom(type)) {
            ByteCodec.writeString(buf, object.toString());
            return;
        }

        validateAsPrimitive(type);

        if (isBoolean(type)) {
            buf.writeBoolean((Boolean) object);
            return;
        } else if (isAssignableFrom(int.class, type)) {
            buf.writeInt((Integer) object);
            return;
        } else if (isAssignableFrom(long.class, type)) {
            buf.writeLong((Long) object);
            return;
        } else if (isAssignableFrom(double.class, type)) {
            buf.writeDouble((Double) object);
            return;
        } else if (isAssignableFrom(float.class, type)) {
            buf.writeFloat((Float) object);
            return;
        } else if (isAssignableFrom(byte.class, type)) {
            buf.writeByte((Byte) object);
            return;
        } else if (isAssignableFrom(short.class, type)) {
            buf.writeShort((Short) object);
            return;
        }

        throw new MessageTransferException(type + " not primitive");
    }
}
