package me.moonways.bridgenet.mtp.message.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.mtp.config.MTPConfiguration;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.MessageRegistry;
import me.moonways.bridgenet.mtp.message.MessageWrapper;
import me.moonways.bridgenet.mtp.message.encryption.MessageEncryption;
import me.moonways.bridgenet.mtp.message.exception.MessageCodecException;
import me.moonways.bridgenet.mtp.message.exception.MessageNotFoundException;
import me.moonways.bridgenet.mtp.transfer.ByteCompression;
import me.moonways.bridgenet.mtp.transfer.MessageTransfer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;

@Log4j2
@RequiredArgsConstructor
public class MessageDecoder extends ByteToMessageDecoder {

    private final MessageRegistry registry;
    private final MTPConfiguration configuration;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        try {
            if (byteBuf.readableBytes() == 0) {
                log.error(new MessageNotFoundException("Get empty packet from decoder"));
                return;
            }

            int messageId = byteBuf.readInt();
            ExportedMessage message = decodeMessage(messageId, byteBuf);

            if (message != null) {
                list.add(message);
            }
        } catch (IndexOutOfBoundsException exception) {
            caughtException(byteBuf, exception);
        } finally {
            byteBuf.skipBytes(byteBuf.readableBytes());
        }
    }

    private ExportedMessage decodeMessage(int messageId, ByteBuf byteBuf) {
        MessageWrapper wrapper = registry.lookupWrapperByID(messageId);

        if (wrapper == null) {
            log.error(new MessageNotFoundException("Decoded message (ID: " + messageId + ") is`nt registered"));
            return null;
        }

        MessageTransfer messageTransfer = createTransfer(byteBuf, wrapper);

        Object message = wrapper.allocate();
        messageTransfer.unbuf(message);

        return new ExportedMessage(wrapper, message);
    }

    private MessageTransfer createTransfer(ByteBuf byteBuf, MessageWrapper wrapper) {
        try {
            ByteBuf buf = ByteCompression.read(byteBuf);

            if (wrapper.needsEncryption()) {

                MessageEncryption encryption = configuration.getEncryption();
                buf = encryption.decode(buf);
            }

            return MessageTransfer.decode(buf);
        }
        catch (DataFormatException | IOException exception) {
            log.error(new MessageCodecException(exception));
            return null;
        }
    }

    private void caughtException(ByteBuf byteBuf, Exception exception) {
        byte[] array = byteBuf.array();

        String asString = new String(array);
        String asArray = Arrays.toString(array);

        log.error("§4Failed to read {} bytes from an incoming packet of {} bytes: §c{asArray={}, asString=\"{}\"}",
                byteBuf.readableBytes(), array.length, asArray, asString,
                new MessageCodecException(exception));
    }
}
