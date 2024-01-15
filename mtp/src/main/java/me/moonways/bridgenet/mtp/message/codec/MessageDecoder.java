package me.moonways.bridgenet.mtp.message.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.mtp.config.MTPConfiguration;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.encryption.MessageEncryption;
import me.moonways.bridgenet.mtp.transfer.ByteCompression;
import me.moonways.bridgenet.mtp.exception.CompressionException;
import me.moonways.bridgenet.mtp.message.MessageWrapper;
import me.moonways.bridgenet.mtp.message.MessageRegistry;
import me.moonways.bridgenet.mtp.message.exception.MessageNotFoundException;
import me.moonways.bridgenet.mtp.transfer.MessageTransfer;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@RequiredArgsConstructor
public class MessageDecoder extends ByteToMessageDecoder {

    private final MessageRegistry registry;
    private final MTPConfiguration configuration;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        try {
            if (byteBuf.readableBytes() == 0) {
                throw new MessageNotFoundException("Get empty packet from decoder");
            }

            int messageId = byteBuf.readIntLE();

            ExportedMessage message = decodeMessage(messageId, byteBuf);
            list.add(message);
        }
        finally {
            byteBuf.skipBytes(byteBuf.readableBytes());
        }
    }

    private ExportedMessage decodeMessage(int messageId, ByteBuf byteBuf) {
        MessageWrapper wrapper = registry.lookupWrapperByID(messageId);

        if (wrapper == null) {
            throw new MessageNotFoundException("Decoded message (ID: " + messageId + ") is`nt registered");
        }

        MessageTransfer messageTransfer = createTransfer(byteBuf, wrapper);

        Object message = wrapper.allocate();
        messageTransfer.unbuf(message);

        return new ExportedMessage(wrapper, message);
    }

    private MessageTransfer createTransfer(ByteBuf byteBuf, MessageWrapper wrapper) {
        try {
            byte[] array = ByteCompression.read(byteBuf);

            if (wrapper.needsEncryption()) {

                MessageEncryption encryption = configuration.getEncryption();
                array = encryption.decode(array);
            }

            return MessageTransfer.decode(array);
        }
        catch (DataFormatException | IOException exception) {
            throw new CompressionException(exception);
        }
    }
}
