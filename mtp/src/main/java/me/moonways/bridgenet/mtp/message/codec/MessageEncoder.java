package me.moonways.bridgenet.mtp.message.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.mtp.transfer.ByteCompression;
import me.moonways.bridgenet.mtp.message.MessageWrapper;
import me.moonways.bridgenet.mtp.message.MessageRegistry;
import me.moonways.bridgenet.mtp.message.exception.MessageNotFoundException;
import me.moonways.bridgenet.mtp.transfer.MessageTransfer;
import me.moonways.bridgenet.injection.Inject;

import java.io.IOException;

@RequiredArgsConstructor
public class MessageEncoder extends MessageToByteEncoder<MessageWrapper> {

    @Inject
    private final MessageRegistry messageRegistry;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageWrapper message, ByteBuf byteBuf) {
        if (message == null) {
            throw new MessageNotFoundException("Packet in encoder is null");
        }

        MessageWrapper wrapper = messageRegistry.lookupWrapperByClass(message.getClass());

        MessageTransfer messageTransfer = new MessageTransfer(message, null);
        messageTransfer.buf();

        try {
            byteBuf.writeIntLE(wrapper.getId());
            ByteCompression.write(messageTransfer.getBytes(), byteBuf);
        }
        catch (IOException exception) {
            throw new ChannelException(exception);
        }
    }
}

