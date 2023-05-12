package me.moonways.bridgenet.protocol.pipeline.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageRegistrationService;
import me.moonways.bridgenet.protocol.exception.MessageDecoderEmptyPacketException;
import me.moonways.bridgenet.protocol.transfer.MessageTransfer;
import me.moonways.bridgenet.protocol.transfer.TransferAllocator;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

@RequiredArgsConstructor
public class MessageDecoder extends ByteToMessageDecoder {

    private final TransferAllocator transferAllocator = new TransferAllocator();

    private static final boolean USE_GZIP = Boolean.TRUE;

    private final Deflater deflater = new Deflater(Deflater.FILTERED, USE_GZIP);
    private final Inflater inflater = new Inflater(USE_GZIP);

    private ByteArrayOutputStream output;

    private final MessageRegistrationService messageRegistrationService;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        System.out.println("Пришло мсг");

        try {
            if (byteBuf.readableBytes() == 0) {
                throw new MessageDecoderEmptyPacketException("Get empty packet from decoder");
            }

            int messageId = byteBuf.readIntLE();

            byte[] bytes = makeDecompress(byteBuf.array());

            MessageTransfer messageTransfer = new MessageTransfer(null, bytes);

            Class<?> messageClass = messageRegistrationService.getMessageById(messageId);

            Message message = transferAllocator.allocatePacket(messageClass, messageTransfer);
            message.setMessageId(messageId);

            list.add(message);
        } finally {
            byteBuf.skipBytes(byteBuf.readableBytes());
        }
    }

    @NotNull
    private OutputStream createCompressOutput() {
        output = new ByteArrayOutputStream();
        return new DeflaterOutputStream(output, deflater);
    }

    @NotNull
    private InputStream createDecompressInput(byte[] array) {
        return new InflaterInputStream(new ByteArrayInputStream(array), inflater);
    }

    private byte[] makeDecompress(byte[] array) {
        try (InputStream compressInput = createDecompressInput(array);
             OutputStream compressOutput = createCompressOutput()) {

            int read;
            while ((read = compressInput.read()) != -1) {
                compressOutput.write(read);
            }

            return output.toByteArray();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
