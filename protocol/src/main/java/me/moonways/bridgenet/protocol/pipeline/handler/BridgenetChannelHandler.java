package me.moonways.bridgenet.protocol.pipeline.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.protocol.message.BridgenetMessageHandler;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageContainer;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BridgenetChannelHandler extends SimpleChannelInboundHandler<Message> {

    private final BridgenetMessageHandler messageHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) {
        if (message.isResponsible()) {
            int messageResponseId = message.getResponseMessageId();

            handleResponse(messageResponseId, message);
            return;
        }

        messageHandler.handle(message.getMessageId(), message);
    }

    private void handleResponse(int messageResponseId, @NotNull Message message) {
        MessageContainer messageContainer = messageHandler.getMessageContainer();
        messageContainer.handleResponse(messageResponseId, message);
    }
}
