package me.moonways.bridgenet.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageResponse;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BridgenetChannel {

    private final Channel channel;
    private final ProtocolControl protocolControl;

    @SneakyThrows
    public <M extends Message> MessageResponse<M> sendMessage(@NotNull Message message, boolean callback) {
        ChannelFuture channelFuture = channel.writeAndFlush(message);
        MessageResponse<M> messageResponse;

        if (callback) {
            messageResponse = new MessageResponse<>();

            int responseId = getNextAwaitResponseMessageId();

            message.setResponseId(responseId);
            addAwaitResponseMessage(responseId, messageResponse);

        } else {
            messageResponse = new MessageResponse<>();
        }

        addChannelListener(channelFuture, messageResponse);
        return messageResponse;
    }

    private void addChannelListener(@NotNull ChannelFuture channelFuture, @NotNull MessageResponse<?> messageResponse) {
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                messageResponse.complete(null);
            } else {
                Throwable cause = future.cause();

                messageResponse.throwException(cause);
            }
        });
    }

    private void addAwaitResponseMessage(int id, @NotNull MessageResponse<?> messageResponse) {
        protocolControl.addResponse(id, messageResponse);
    }

    private int getNextAwaitResponseMessageId() {
        return protocolControl.getNextAwaitResponseMessageId();
    }

    public synchronized void close() {
        channel.closeFuture();
    }
}
