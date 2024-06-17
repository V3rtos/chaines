package me.moonways.bridgenet.mtp.message.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.profiler.BridgenetDataLogger;
import me.moonways.bridgenet.profiler.ProfilerType;
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
    private BridgenetDataLogger bridgenetDataLogger;

    @Override
    protected synchronized void encode(ChannelHandlerContext channelHandlerContext, ExportedMessage exportedMessage, ByteBuf byteBuf) {
        if (exportedMessage == null || exportedMessage.getMessage() == null || exportedMessage.getWrapper() == null) {
            throw new MessageCodecException("Can`t encode " + exportedMessage);
        }

        WrappedNetworkMessage wrapper = exportedMessage.getWrapper();
        Object message = exportedMessage.getMessage();

        try {
            MessageTransfer messageTransfer = MessageTransfer.encode(message);
            messageTransfer.buf();

            byteBuf.writeInt(wrapper.getId());
            byteBuf.writeLong(exportedMessage.getCallbackID());

            ByteBuf buffer = messageTransfer.getByteBuf();

            if (wrapper.needsEncryption()) {

                MessageEncryption encryption = configuration.getEncryption();
                buffer = encryption.encode(buffer);

                if (buffer == null) {
                    byteBuf.clear();
                    return;
                }
            }

            byte[] array = ByteCodec.readBytesArray(buffer);
            ByteCompression.write(array, byteBuf);

            bridgenetDataLogger.logWritesCount(ProfilerType.MTP_TRAFFIC, byteBuf.readableBytes());
        } catch (Exception exception) {
            throw new MessageCodecException(exception);
        }
    }
}

