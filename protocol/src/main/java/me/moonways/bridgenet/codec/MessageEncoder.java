package me.moonways.bridgenet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.Message;
import me.moonways.bridgenet.MessageRegistryContainer;
import me.moonways.bridgenet.exception.MessageEncoderPacketIsNullException;
import me.moonways.bridgenet.transfer.MessageTransfer;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

@RequiredArgsConstructor
public class MessageEncoder extends MessageToByteEncoder<Message> {

    private final MessageRegistryContainer messageRegistryContainer;

    private static final boolean USE_GZIP = Boolean.TRUE;

    private final Deflater deflater = new Deflater(Deflater.FILTERED, USE_GZIP);

    private ByteArrayOutputStream output;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        if (message == null) {
            throw new MessageEncoderPacketIsNullException("Packet in encoder is null");
        }

        int messageId = messageRegistryContainer.getIdByMessage(message.getClass());

        MessageTransfer messageTransfer = new MessageTransfer(message, null);

        writeMessageId(byteBuf, messageId);
        byteBuf.writeBytes(messageTransfer.getBytes());
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
