package me.moonways.bridgenet.mtp.message.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.mtp.config.MTPConfiguration;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.MessageWrapper;
import me.moonways.bridgenet.mtp.message.encryption.MessageEncryption;
import me.moonways.bridgenet.mtp.transfer.ByteCompression;
import me.moonways.bridgenet.mtp.transfer.MessageTransfer;

@RequiredArgsConstructor
public class MessageEncoder extends MessageToByteEncoder<ExportedMessage> {

    private final MTPConfiguration configuration;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ExportedMessage exportedMessage, ByteBuf byteBuf) {
        if (exportedMessage == null || exportedMessage.getMessage() == null || exportedMessage.getWrapper() == null) {
            throw new NullPointerException("encoding for " + exportedMessage);
        }

        MessageWrapper wrapper = exportedMessage.getWrapper();
        Object message = exportedMessage.getMessage();

        try {
            MessageTransfer messageTransfer = MessageTransfer.encode(message);
            messageTransfer.buf();

            byteBuf.writeIntLE(wrapper.getId());
            byte[] messageBytes = messageTransfer.getBytes();

            if (wrapper.needsEncryption()) {

                MessageEncryption encryption = configuration.getEncryption();
                messageBytes = encryption.encode(messageBytes);
            }

            ByteCompression.write(messageBytes, byteBuf);
        }
        catch (Exception exception) {
            throw new ChannelException(exception);
        }
    }
}

