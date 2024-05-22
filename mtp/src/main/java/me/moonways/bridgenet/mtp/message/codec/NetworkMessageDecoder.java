package me.moonways.bridgenet.mtp.message.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.metrics.BridgenetMetricsLogger;
import me.moonways.bridgenet.metrics.MetricType;
import me.moonways.bridgenet.mtp.config.NetworkJsonConfiguration;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.NetworkMessagesService;
import me.moonways.bridgenet.mtp.message.WrappedNetworkMessage;
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
public class NetworkMessageDecoder extends ByteToMessageDecoder {

    private final NetworkMessagesService registry;
    private final NetworkJsonConfiguration configuration;

    @Inject
    private BridgenetMetricsLogger bridgenetMetricsLogger;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        try {
            int readableBytes = byteBuf.readableBytes();
            if (readableBytes == 0) {
                log.error(new MessageNotFoundException("Get empty packet from decoder"));
                return;
            }

            bridgenetMetricsLogger.logNetworkTrafficBytesRead(MetricType.MTP_TRAFFIC, readableBytes);

            int messageId = byteBuf.readInt();
            ExportedMessage message = decodeMessage(messageId, byteBuf);

            if (message != null) {
                list.add(message);
            }
        } catch (IndexOutOfBoundsException exception) {
            caughtBytesReadException(byteBuf, exception);
        } finally {
            byteBuf.skipBytes(byteBuf.readableBytes());
        }
    }

    private ExportedMessage decodeMessage(int messageId, ByteBuf byteBuf) {
        WrappedNetworkMessage wrapper = registry.lookupWrapperByID(messageId);

        if (wrapper == null) {
            log.error(new MessageNotFoundException("Decoded message (ID: " + messageId + ") is`nt registered"));
            return null;
        }

        MessageTransfer messageTransfer = createTransfer(byteBuf, wrapper);

        Object message = wrapper.createObject();
        messageTransfer.unbuf(message);

        return new ExportedMessage(wrapper, message);
    }

    private MessageTransfer createTransfer(ByteBuf byteBuf, WrappedNetworkMessage wrapper) {
        try {
            ByteBuf buf = ByteCompression.read(byteBuf);

            if (wrapper.needsEncryption()) {

                MessageEncryption encryption = configuration.getEncryption();
                buf = encryption.decode(buf);
            }

            return MessageTransfer.decode(buf);
        } catch (DataFormatException | IOException exception) {
            log.error(new MessageCodecException(exception));
            return null;
        }
    }

    private void caughtBytesReadException(ByteBuf byteBuf, Exception exception) {
        byte[] array = byteBuf.array();

        String asString = new String(array);
        String asArray = Arrays.toString(array);

        log.error("§4Failed to read {} bytes from an incoming packet of {} bytes: §c{asArray={}, asString=\"{}\"}",
                byteBuf.readableBytes(), array.length, asArray, asString,
                new MessageCodecException(exception));
    }
}
