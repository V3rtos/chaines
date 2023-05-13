package me.moonways.bridgenet.protocol.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.protocol.ProtocolControl;
import me.moonways.bridgenet.protocol.message.MessageTriggerHandler;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.pipeline.exception.ChannelHandlerException;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Log4j2
public class BridgenetChannelHandler extends SimpleChannelInboundHandler<Message> {

    private final ProtocolControl protocolControl;
    private final MessageTriggerHandler triggerHandler;

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
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) {
        if (message.isResponsible()) {
            handleResponse(message.getResponseId(), message);
            //return;
        }

        triggerHandler.fireTriggers(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        throw new ChannelHandlerException(cause, "Internal channel handling error");
    }

    private void handleResponse(int messageResponseId, @NotNull Message message) {
        protocolControl.handleResponseFuture(messageResponseId, message);
    }
}
