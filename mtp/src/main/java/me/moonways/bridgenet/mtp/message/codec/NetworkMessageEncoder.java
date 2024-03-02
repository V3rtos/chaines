package me.moonways.bridgenet.mtp.message.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.metrics.BridgenetMetricsLogger;
import me.moonways.bridgenet.metrics.MetricType;
import me.moonways.bridgenet.mtp.config.NetworkJsonConfiguration;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.WrappedNetworkMessage;
import me.moonways.bridgenet.mtp.message.encryption.MessageEncryption;
import me.moonways.bridgenet.mtp.message.exception.MessageCodecException;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.ByteCompression;
import me.moonways.bridgenet.mtp.transfer.MessageTransfer;

@Log4j2
@RequiredArgsConstructor
public class NetworkMessageEncoder extends MessageToByteEncoder<ExportedMessage> {

    private final NetworkJsonConfiguration configuration;

    @Inject
    private BridgenetMetricsLogger bridgenetMetricsLogger;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ExportedMessage exportedMessage, ByteBuf byteBuf) {
        if (exportedMessage == null || exportedMessage.getMessage() == null || exportedMessage.getWrapper() == null) {
            log.error(new MessageCodecException("can not encode " + exportedMessage + " null"));
            return;
        }

        WrappedNetworkMessage wrapper = exportedMessage.getWrapper();
        Object message = exportedMessage.getMessage();

        try {
            MessageTransfer messageTransfer = MessageTransfer.encode(message);
            messageTransfer.buf();

            byteBuf.writeInt(wrapper.getId());
            ByteBuf buffer = messageTransfer.getByteBuf();

            if (wrapper.needsEncryption()) {

                MessageEncryption encryption = configuration.getEncryption();
                buffer = encryption.encode(buffer);
            }

            byte[] array = ByteCodec.readBytesArray(buffer);
            ByteCompression.write(array, byteBuf);

            bridgenetMetricsLogger.logNetworkTrafficBytesWrite(MetricType.MTP_TRAFFIC, byteBuf.writableBytes());
        }
        catch (Exception exception) {
            log.error(new MessageCodecException(exception));
        }
    }
}

