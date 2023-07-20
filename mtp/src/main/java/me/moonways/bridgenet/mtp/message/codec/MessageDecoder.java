package me.moonways.bridgenet.mtp.message.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.mtp.message.DecodedMessage;
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

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        try {
            if (byteBuf.readableBytes() == 0) {
                throw new MessageNotFoundException("Get empty packet from decoder");
            }

            int messageId = byteBuf.readIntLE();

            DecodedMessage message = decodeMessage(messageId, byteBuf);
            list.add(message);
        }
        finally {
            byteBuf.skipBytes(byteBuf.readableBytes());
        }
    }

    private DecodedMessage decodeMessage(int messageId, ByteBuf byteBuf) {
        MessageTransfer messageTransfer = createTransfer(byteBuf);
        MessageWrapper wrapper = registry.lookupWrapperByID(messageId);

        Object message = wrapper.allocate();
        messageTransfer.unbuf(message);

        return new DecodedMessage(wrapper, message);
    }

    private MessageTransfer createTransfer(ByteBuf byteBuf) {
        try {
            byte[] array = ByteCompression.read(byteBuf);
            return new MessageTransfer(null, array);
        }
        catch (DataFormatException | IOException exception) {
            throw new CompressionException(exception);
        }
    }
}
