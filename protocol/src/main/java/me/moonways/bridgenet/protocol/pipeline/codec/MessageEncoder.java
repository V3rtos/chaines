package me.moonways.bridgenet.protocol.pipeline.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageRegistrationService;
import me.moonways.bridgenet.protocol.exception.MessageEncoderPacketIsNullException;
import me.moonways.bridgenet.protocol.message.TestMessage;
import me.moonways.bridgenet.protocol.transfer.MessageTransfer;
import me.moonways.bridgenet.service.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

@RequiredArgsConstructor
public class MessageEncoder extends MessageToByteEncoder<Message> {

    private static final boolean USE_GZIP = Boolean.TRUE;

    private final Deflater deflater = new Deflater(Deflater.FILTERED, USE_GZIP);

    private ByteArrayOutputStream output;

    @Inject
    private final MessageRegistrationService messageRegistrationService;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) {
        if (message == null) {
            throw new MessageEncoderPacketIsNullException("Packet in encoder is null");
        }

        int messageId = messageRegistrationService.getIdByMessage(message.getClass());

        MessageTransfer messageTransfer = new MessageTransfer(message, null);
        messageTransfer.buf(); // TODO: 12.05.2023

        byteBuf.writeIntLE(messageId);
        //writeMessageId(byteBuf, messageId);
        
        //byte[] compressedBytes = makeCompress(messageTransfer.getBytes()); // TODO: 12.05.2023
        byte[] compressedBytes = messageTransfer.getBytes();

        byteBuf.writeBytes(compressedBytes);
    }

    private void writeMessageId(ByteBuf byteBuf, int messageId) {
        while ((messageId & -128) != 0) {
            byteBuf.writeByte(messageId & 127 | 128);
            messageId >>>= 7;
        }

        byteBuf.writeByte(messageId);
    }

    @NotNull
    private OutputStream createCompressOutput() {
        output = new ByteArrayOutputStream();
        return new DeflaterOutputStream(output, deflater);
    }

    private byte[] makeCompress(byte[] array) {
        try (OutputStream compressOutput = createCompressOutput()) {

            compressOutput.write(array);
            compressOutput.flush();

            return output.toByteArray();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
