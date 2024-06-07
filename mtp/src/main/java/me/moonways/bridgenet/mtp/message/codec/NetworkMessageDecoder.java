package me.moonways.bridgenet.mtp.message.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.transfer.MessageTransferException;
import me.moonways.bridgenet.profiler.BridgenetDataLogger;
import me.moonways.bridgenet.profiler.ProfilerType;
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
    private BridgenetDataLogger bridgenetDataLogger;

    @Override
    protected synchronized void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        try {
            int readableBytes = byteBuf.readableBytes();
            if (readableBytes == 0) {
                throw new MessageNotFoundException("Get empty packet from decoder");
            }

            bridgenetDataLogger.logReadsCount(ProfilerType.MTP_TRAFFIC, readableBytes);

            int messageId = byteBuf.readInt();
            ExportedMessage message = decodeMessage(messageId, byteBuf, channelHandlerContext);

            list.add(message);

        } catch (IndexOutOfBoundsException exception) {
            NetworkCodecDumps.dump(channelHandlerContext, byteBuf);
            throw new MessageTransferException(exception);
        } finally {
            byteBuf.skipBytes(byteBuf.readableBytes());
        }
    }

    private ExportedMessage decodeMessage(int messageId, ByteBuf byteBuf, ChannelHandlerContext channelHandlerContext) {
        WrappedNetworkMessage wrapper = registry.lookupWrapperByID(messageId);

        if (wrapper == null) {
            NetworkCodecDumps.dump(channelHandlerContext, byteBuf);
            throw new MessageNotFoundException("Decoded message (ID: " + messageId + ") is`nt registered");
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
            throw new MessageCodecException(exception);
        }
    }
}
