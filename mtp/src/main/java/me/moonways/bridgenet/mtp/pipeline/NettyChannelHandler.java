package me.moonways.bridgenet.mtp.pipeline;

import io.netty.channel.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.ExceptionallyConsumer;
import me.moonways.bridgenet.metrics.BridgenetMetricsLogger;
import me.moonways.bridgenet.metrics.MetricType;
import me.moonways.bridgenet.mtp.MTPChannel;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.mtp.ProtocolDirection;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.InputMessageContext;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class NettyChannelHandler extends SimpleChannelInboundHandler<ExportedMessage> {

    private final List<ChannelHandler> afterHandlersList;
    private final MTPDriver driver;

    @Inject
    private BridgenetMetricsLogger bridgenetMetricsLogger;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        log.info("§9New connection attempt was detected: {}", ctx.channel());
        fireAfterInboundHandlers(subj -> subj.channelRegistered(ctx));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("§7The activation of a new connection channel is played:");
        log.info("  §7- Channel ID of the new connection: {}", ctx.channel().id());
        log.info("  §7- Remote channel connection address: {}", ctx.channel().remoteAddress());

        fireAfterInboundHandlers(subj -> subj.channelActive(ctx));
        bridgenetMetricsLogger.logNetworkConnectionOpened(MetricType.MTP_CONNECTIONS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("§4Connected client connection channel has been severed from the server: (ID={})", ctx.channel().id());

        fireAfterInboundHandlers(subj -> subj.channelInactive(ctx));
        bridgenetMetricsLogger.logNetworkConnectionClosed(MetricType.MTP_CONNECTIONS);
    }

    private MTPChannel newHandlerChannel(MTPChannel channel) {
        ProtocolDirection direction = channel.getDirection();
        ProtocolDirection newDirection = direction == ProtocolDirection.TO_CLIENT ? ProtocolDirection.TO_SERVER : ProtocolDirection.TO_CLIENT;

        MTPChannel mtpChannel = new MTPChannel(newDirection, channel.getHandle());

        driver.getBeansService().inject(mtpChannel);

        return mtpChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExportedMessage message) {
        Channel channel = ctx.channel();

        MTPChannel mtpChannel = newHandlerChannel(
                new MTPChannel(channel.attr(MTPChannel.DIRECTION_ATTRIBUTE).get(), channel));

        log.info("§9[{}]: §r{}", String.format(mtpChannel.getMessageSendLogPrefix(), channel.remoteAddress()), message.getMessage());
        driver.handle(new InputMessageContext<>(message.getMessage(), newHandlerChannel(mtpChannel), System.currentTimeMillis()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("§4MessageTransferProtocol channel handler has received internal exception", cause);
    }

    private void fireAfterInboundHandlers(ExceptionallyConsumer<ChannelInboundHandler> consumer) {
        for (ChannelHandler subj : afterHandlersList) {

            if (subj instanceof ChannelInboundHandler) {
                try {
                    consumer.accept((ChannelInboundHandler) subj);
                } catch (Throwable exception) {
                    log.error(exception.getMessage(), exception);
                }
            }
        }
    }
}
