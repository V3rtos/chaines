package me.moonways.bridgenet.mtp.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.mtp.message.ExportedMessage;

@RequiredArgsConstructor
@Log4j2
public class MTPChannelHandler extends SimpleChannelInboundHandler<ExportedMessage> {

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
        log.info("§4The previously connected client connection channel has been severed from the server: (ID={})", ctx.channel().id());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExportedMessage message) {
        log.info("§9[Client[ID={}] -> Server]: §r{}", ctx.channel().id(), message.getMessage());
        driver.handle(message.getWrapper(), message.getMessage());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("§4Internal MTP exception caught: §c{}", cause.toString());
    }
}
