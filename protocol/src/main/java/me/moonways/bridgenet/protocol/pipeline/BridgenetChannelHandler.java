package me.moonways.bridgenet.protocol.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.protocol.message.MessageTriggersProvider;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageContainer;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BridgenetChannelHandler extends SimpleChannelInboundHandler<Message> {

    private final MessageContainer messageContainer;

    private final MessageTriggersProvider triggersProvider;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) {
        if (message.isResponsible()) {
            int messageResponseId = message.getResponseMessageId();

            handleResponse(messageResponseId, message);
            return;
        }

        triggersProvider.fireTriggers(message);
    }

    private void handleResponse(int messageResponseId, @NotNull Message message) {
        messageContainer.handleResponse(messageResponseId, message);
    }
}
