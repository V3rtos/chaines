package me.moonways.bridgenet.protocol.pipeline.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.protocol.compression.CompressionUtils;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageRegistrationService;
import me.moonways.bridgenet.protocol.exception.MessageDecoderEmptyPacketException;
import me.moonways.bridgenet.protocol.transfer.MessageTransfer;
import me.moonways.bridgenet.protocol.transfer.TransferAllocator;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;
import java.util.zip.*;

@RequiredArgsConstructor
public class MessageDecoder extends ByteToMessageDecoder {

    private final TransferAllocator transferAllocator = new TransferAllocator();

    private final MessageRegistrationService messageRegistrationService;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        try {
            if (byteBuf.readableBytes() == 0) {
                throw new MessageDecoderEmptyPacketException("Get empty packet from decoder");
            }

            int messageId = byteBuf.readIntLE();

            int responseId = 0;
            boolean response = byteBuf.readBoolean();

            if (response) {
                responseId = byteBuf.readIntLE();
            }

            MessageTransfer messageTransfer = createTransfer(byteBuf);

            Class<?> messageClass = messageRegistrationService.getMessageById(messageId);

            Message message = transferAllocator.allocatePacket(messageClass, messageTransfer);

            message.setChannel(channelHandlerContext.channel());
            message.setMessageId(messageId);
            message.setResponseId(responseId);

            list.add(message);
        } finally {
            byteBuf.skipBytes(byteBuf.readableBytes());
        }
    }

    private MessageTransfer createTransfer(ByteBuf byteBuf) {
        try {
            byte[] array = CompressionUtils.read(byteBuf);
            return new MessageTransfer(null, array);
        }
        catch (DataFormatException | IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
