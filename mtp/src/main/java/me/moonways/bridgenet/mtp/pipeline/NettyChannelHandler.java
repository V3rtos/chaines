package me.moonways.bridgenet.mtp.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.mtp.message.DecodedMessage;
import me.moonways.bridgenet.mtp.message.MessageWrapper;
import me.moonways.bridgenet.mtp.pipeline.exception.ChannelPipelineException;

@RequiredArgsConstructor
@Log4j2
public class NettyChannelHandler extends SimpleChannelInboundHandler<DecodedMessage> {

    private final MTPDriver driver;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("Activated channel: " + ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        log.info("Registered channel: " + ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        log.info("Unregistered channel: " + ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DecodedMessage message) {
        driver.handle(message.getWrapper(), message.getMessage());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        throw new ChannelPipelineException(cause, "Internal channel handling error");
    }
}
