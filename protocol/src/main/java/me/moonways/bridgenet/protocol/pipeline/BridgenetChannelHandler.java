package me.moonways.bridgenet.protocol.pipeline;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import me.moonways.bridgenet.protocol.ProtocolControl;
import me.moonways.bridgenet.protocol.message.MessageComponent;
import me.moonways.bridgenet.protocol.message.MessageState;
import me.moonways.bridgenet.protocol.message.MessageTriggerHandler;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.pipeline.exception.ChannelHandlerException;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Log4j2
public class BridgenetChannelHandler extends SimpleChannelInboundHandler<Message> {

    private final ProtocolControl protocolControl;
    private final MessageTriggerHandler triggerHandler;

    private BridgenetChannel bridgenetChannel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        initializeChannel(ctx.channel());

        log.info("Activated channel: " + ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        log.info("Registered channel: " + ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        unregisterChannel();

        log.info("Unregistered channel: " + ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) {
        initializeMessageChannel(message);

        if (message.getClass().getDeclaredAnnotation(MessageComponent.class).state().equals(MessageState.RESPONSE)) {
            handleResponse(message.getResponseId(), message);
            return;
        }

        triggerHandler.fireTriggers(message);
    }

    private void initializeMessageChannel(@NotNull Message message) {
        message.setChannel(bridgenetChannel); // Устанавливаем канал сообщению для удобства манипуляций с ним
    }

    private void initializeChannel(@NotNull Channel channel) {
        bridgenetChannel = new BridgenetChannel(channel, protocolControl);
    }

    private void unregisterChannel() {
        bridgenetChannel = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        throw new ChannelHandlerException(cause, "Internal channel handling error");
    }

    private void handleResponse(int messageResponseId, @NotNull Message message) {
        protocolControl.handleResponseFuture(messageResponseId, message);
    }
}
