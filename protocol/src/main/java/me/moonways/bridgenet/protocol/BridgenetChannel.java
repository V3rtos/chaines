package me.moonways.bridgenet.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.moonways.bridgenet.protocol.message.Message;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class BridgenetChannel {

    private final Channel channel;
    private final ProtocolControl protocolControl;

    public void sendMessage(@NotNull Message message) {
        sendMessage(message, false);
    }

    @SneakyThrows
    public <M extends Message> CompletableFuture<M> sendMessageWithCallback(@NotNull Message message) {
        return sendMessage(message, true);
    }
    @SneakyThrows
    private <M extends Message> CompletableFuture<M> sendMessage(@NotNull Message message, boolean callback) {
        CompletableFuture<M> messageResponseHandler = new CompletableFuture<>();

        if (callback) {
            int responseId = protocolControl.nextIncrementedResponseID();

            message.setResponseId(responseId);
            saveResponseHandler(responseId, messageResponseHandler);
        }

        ChannelFuture channelFuture = channel.writeAndFlush(message);
        channelFuture.addListener(future -> {

            if (!future.isSuccess()) {
                messageResponseHandler.completeExceptionally(future.cause());
                return;
            }

            messageResponseHandler.complete(null);
        });
        
        return messageResponseHandler;
    }
    private void saveResponseHandler(int id, @NotNull CompletableFuture<? extends Message> messageResponse) {
        protocolControl.saveResponseHandler(id, messageResponse);
    }
    public synchronized void close() {
        channel.closeFuture();
    }
}
