package me.moonways.bridgenet.mtp.message.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.mtp.BridgenetChannelException;

import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;

@Log4j2
@UtilityClass
public class NetworkCodecDumps {

    private static final String DUMP_FORMAT =
            "\n// ========================= // MTP DUMP // ========================= //\n" +
            "Channel={id=%s, remoteAddress=%s, locaAddress=%s};\n" +
            "ByteBuf={bytes=%d}\n" +
            "\n" +
            "ByteBuf content as byte[]: %s\n" +
            "ByteBuf content as String: \"%s\"\n" +
            "// ========================= // MTP DUMP // ========================= //";

    public synchronized void dump(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        Channel channel = channelHandlerContext.channel();
        try {
            log.debug(dumpToString(channel, byteBuf));
        } catch (DataFormatException | IOException exception) {
            throw new BridgenetChannelException(exception);
        }
    }

    private String dumpToString(Channel channel, ByteBuf byteBuf) throws DataFormatException, IOException {
        byte[] array = new byte[byteBuf.readableBytes()];

        byteBuf.readBytes(array);

        String asString = new String(array);
        String asArray = Arrays.toString(array);

        return String.format(DUMP_FORMAT, channel.id(), channel.remoteAddress(), channel.localAddress(),
                array.length,
                asArray,
                asString);
    }
}
