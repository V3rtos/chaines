package me.moonways.bridgenet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BridgenetChannelHandler extends SimpleChannelInboundHandler<Message> {

    private final BridgenetMessageHandler messageHandler;

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        messageHandler.handleChannelActive(ctx);
    }

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
        messageHandler.getResponseHandler().handleResponse(messageResponseId, message);
    }
}
