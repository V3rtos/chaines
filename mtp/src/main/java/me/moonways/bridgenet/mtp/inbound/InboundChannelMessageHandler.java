package me.moonways.bridgenet.mtp.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.ExceptionallyConsumer;
import me.moonways.bridgenet.profiler.BridgenetDataLogger;
import me.moonways.bridgenet.profiler.ProfilerType;
import me.moonways.bridgenet.mtp.BridgenetNetworkController;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.mtp.channel.ChannelDirection;
import me.moonways.bridgenet.mtp.channel.NetworkChannelFactory;
import me.moonways.bridgenet.mtp.channel.NetworkRemoteChannel;
import me.moonways.bridgenet.mtp.event.ChannelDownstreamEvent;
import me.moonways.bridgenet.mtp.event.ChannelOpenedEvent;
import me.moonways.bridgenet.mtp.event.ChannelReadEvent;
import me.moonways.bridgenet.mtp.event.ChannelRegisteredEvent;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.InboundMessageContext;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class InboundChannelMessageHandler extends SimpleChannelInboundHandler<ExportedMessage> {

    private final ChannelDirection channelDirection;
    private final List<ChannelInboundHandler> childrenHandlers;

    @Inject
    private BridgenetDataLogger bridgenetDataLogger;
    @Inject
    private BridgenetNetworkController networkController;
    @Inject
    private NetworkChannelFactory networkChannelFactory;
    @Inject
    private EventService eventService;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        if (channelDirection == ChannelDirection.TO_CLIENT) { // is server currently
            log.info("§9New connection attempt was detected: {}", ctx.channel());
        }

        eventService.fireEvent(new ChannelRegisteredEvent(ctx));
        callChildrenHandlers(child -> child.channelRegistered(ctx));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("§9The activation of a new connection channel is played:");
        log.debug("§7 - Channel ID of the new connection: {}", ctx.channel().id());
        log.debug("§7 - Remote channel connection address: {}", ctx.channel().remoteAddress());

        callChildrenHandlers(child -> child.channelActive(ctx));

        eventService.fireEvent(new ChannelOpenedEvent(ctx, toInboundChannel(ctx)));
        bridgenetDataLogger.logConnectionOpen(ProfilerType.MTP_CONNECTIONS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("§4Connected client connection channel has been severed: (ID={})", ctx.channel().id());

        callChildrenHandlers(child -> child.channelInactive(ctx));

        eventService.fireEvent(new ChannelDownstreamEvent(ctx, toInboundChannel(ctx)));
        bridgenetDataLogger.logConnectionClose(ProfilerType.MTP_CONNECTIONS);
    }

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, ExportedMessage exportedMessage) {
        BridgenetNetworkChannel inboundChannel = toChannel(ctx);
        Object message = exportedMessage.getMessage();

        log.debug("§9[{}]: §r{}",
                String.format(NetworkRemoteChannel.MESSAGE_HANDLE_LOG_MSG.apply(channelDirection.reverse()),
                        ctx.channel().remoteAddress()), message);

        networkController.pull(new InboundMessageContext<>(exportedMessage.getCallbackID(), message, inboundChannel, System.currentTimeMillis()));
        eventService.fireEvent(new ChannelReadEvent(ctx, inboundChannel, exportedMessage));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("§4MTP channel-handler has received internal exception", cause);
    }

    private void callChildrenHandlers(ExceptionallyConsumer<ChannelInboundHandler> consumer) {
        childrenHandlers.forEach(channelHandler -> {
            try {
                consumer.accept(channelHandler);
            } catch (Throwable exception) {
                log.error(exception.getMessage(), exception);
            }
        });
    }

    private BridgenetNetworkChannel toInboundChannel(ChannelHandlerContext ctx) {
        return networkChannelFactory.create(ChannelDirection.TO_SERVER, ctx.channel());
    }

    private BridgenetNetworkChannel toChannel(ChannelHandlerContext ctx) {
        return networkChannelFactory.create(channelDirection, ctx.channel());
    }
}
