package me.moonways.bridgenet.mtp.inbound;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.ExceptionallyConsumer;
import me.moonways.bridgenet.metrics.BridgenetMetricsLogger;
import me.moonways.bridgenet.metrics.MetricType;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.mtp.channel.ChannelDirection;
import me.moonways.bridgenet.mtp.channel.NetworkChannelFactory;
import me.moonways.bridgenet.mtp.channel.NetworkRemoteChannel;
import me.moonways.bridgenet.mtp.event.ChannelDownstreamEvent;
import me.moonways.bridgenet.mtp.event.ChannelOpenedEvent;
import me.moonways.bridgenet.mtp.event.ChannelReadEvent;
import me.moonways.bridgenet.mtp.event.ChannelRegisteredEvent;
import me.moonways.bridgenet.mtp.message.ExportedMessage;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class InboundChannelMessageHandler extends SimpleChannelInboundHandler<ExportedMessage> {

    private final ChannelDirection channelDirection;
    private final List<ChannelHandler> childrenHandlers;

    @Inject
    private NetworkRemoteChannel networkChannel;
    @Inject
    private BridgenetMetricsLogger bridgenetMetricsLogger;
    @Inject
    private NetworkChannelFactory networkChannelFactory;
    @Inject
    private EventService eventService;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        if (channelDirection == ChannelDirection.TO_CLIENT) {
            log.info("§9New connection attempt was detected: {}", ctx.channel());
        }

        eventService.fireEvent(new ChannelRegisteredEvent(ctx));
        callChildrenHandlers(child -> child.channelRegistered(ctx));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("§7The activation of a new connection channel is played:");
        log.info("§7 - Channel ID of the new connection: {}", ctx.channel().id());
        log.info("§7 - Remote channel connection address: {}", ctx.channel().remoteAddress());

        callChildrenHandlers(child -> child.channelActive(ctx));

        eventService.fireEvent(new ChannelOpenedEvent(ctx, networkChannel));
        bridgenetMetricsLogger.logNetworkConnectionOpened(MetricType.MTP_CONNECTIONS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("§4Connected client connection channel has been severed from the server: (ID={})", ctx.channel().id());

        callChildrenHandlers(child -> child.channelInactive(ctx));

        eventService.fireEvent(new ChannelDownstreamEvent(ctx, networkChannel));
        bridgenetMetricsLogger.logNetworkConnectionClosed(MetricType.MTP_CONNECTIONS);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExportedMessage message) {
        ChannelDirection reversedDirection = channelDirection.reverse();
        String logMessage = NetworkRemoteChannel.MESSAGE_HANDLE_LOG_MSG.apply(reversedDirection);

        log.info("§9[{}]: §r{}", String.format(logMessage, ctx.channel().remoteAddress()), message.getMessage());

        BridgenetNetworkChannel channelReader = networkChannelFactory.create(reversedDirection, ctx.channel());

        eventService.fireEvent(new ChannelReadEvent(ctx, channelReader, message));
        channelReader.pull(message.getMessage());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("§4MTP channel-handler has received internal exception", cause);
    }

    private void callChildrenHandlers(ExceptionallyConsumer<ChannelInboundHandler> consumer) {
        childrenHandlers.forEach(channelHandler -> {
            if (channelHandler instanceof ChannelInboundHandler) {
                try {
                    consumer.accept((ChannelInboundHandler) channelHandler);
                } catch (Throwable exception) {
                    log.error(exception.getMessage(), exception);
                }
            }
        });
    }
}
