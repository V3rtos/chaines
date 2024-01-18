package me.moonways.bridgenet.mtp.pipeline;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.mtp.MTPChannel;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.mtp.ProtocolDirection;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.InputMessageContext;

@RequiredArgsConstructor
@Log4j2
public class NettyChannelHandler extends SimpleChannelInboundHandler<ExportedMessage> {

    private final MTPDriver driver;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        log.info("§9New connection attempt was detected: {}", ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("§7The activation of a new connection channel is played:");
        log.info("  §7- Channel ID of the new connection: {}", ctx.channel().id());
        log.info("  §7- Remote channel connection address: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("§4Connected client connection channel has been severed from the server: (ID={})", ctx.channel().id());
    }

    private MTPChannel newHandlerChannel(MTPChannel channel) {
        ProtocolDirection direction = channel.getDirection();
        ProtocolDirection newDirection = direction == ProtocolDirection.TO_CLIENT ? ProtocolDirection.TO_SERVER : ProtocolDirection.TO_CLIENT;

        MTPChannel mtpChannel = new MTPChannel(newDirection, channel.getHandle());

        driver.getInjector().injectFields(mtpChannel);

        return mtpChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExportedMessage message) {
        MTPChannel channel = newHandlerChannel(
                new MTPChannel(ctx.channel().attr(MTPChannel.DIRECTION_ATTRIBUTE).get(), ctx.channel()));

        log.info("§9[{}]: §r{}", String.format(channel.getMessageSendLogPrefix(), ctx.channel().id()), message.getMessage());
        driver.handle(new InputMessageContext<>(message.getMessage(), newHandlerChannel(channel), System.currentTimeMillis()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("§4MessageTransferProtocol channel handler has received internal exception", cause);
    }
}
