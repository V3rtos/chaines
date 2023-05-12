package me.moonways.bridgenet.protocol.pipeline.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.protocol.compression.CompressionUtils;
import me.moonways.bridgenet.protocol.exception.MessageEncoderPacketIsNullException;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageRegistrationService;
import me.moonways.bridgenet.protocol.transfer.MessageTransfer;
import me.moonways.bridgenet.service.inject.Inject;

import java.io.IOException;

@RequiredArgsConstructor
public class MessageEncoder extends MessageToByteEncoder<Message> {

    @Inject
    private final MessageRegistrationService messageRegistrationService;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) {
        if (message == null) {
            throw new MessageEncoderPacketIsNullException("Packet in encoder is null");
        }

        int messageId = messageRegistrationService.getIdByMessage(message.getClass());

        MessageTransfer messageTransfer = new MessageTransfer(message, null);
        messageTransfer.buf();

        try {
            byteBuf.writeIntLE(messageId);

            byteBuf.writeBoolean(message.isResponsible());

            if (message.isResponsible()) {
                byteBuf.writeIntLE(message.getResponseId());
            }

            CompressionUtils.write(messageTransfer.getBytes(), byteBuf);


        }


        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}

