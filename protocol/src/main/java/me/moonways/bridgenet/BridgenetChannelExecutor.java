package me.moonways.bridgenet;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

public class BridgenetChannelExecutor {

    private final MessageResponseHandler responseHandler;

    @Getter
    private BridgenetChannel channel;

    public BridgenetChannelExecutor(@NotNull MessageResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @SneakyThrows
    @SuppressWarnings({"rawtypes", "unchecked", "UnusedReturnValue"})
    public <M extends Message> MessageResponse<M> sendMessage(@NotNull Message message, @NotNull MessageParameter messageParameter) {
        if (messageParameter.isCallback()) {
            MessageResponse<M> messageResponse = new MessageResponse<>();

            int responseId = getNextAwaitResponseMessageId();

            message.setResponseId(responseId);
            addAwaitResponseMessage(responseId, messageResponse);
            return messageResponse;
        }

        ChannelFuture channelFuture = channel.get()
                .writeAndFlush(message);
        MessageResponse messageResponse = new MessageResponse();

        addChannelListener(channelFuture, messageResponse);
        return messageResponse;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addChannelListener(@NotNull ChannelFuture channelFuture, @NotNull MessageResponse messageResponse) {
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
        responseHandler.addResponse(id, messageResponse);
    }

    private int getNextAwaitResponseMessageId() {
        return responseHandler.getNextAwaitResponseMessageId();
    }

    public void initializeChannelHandlerContext(@NotNull ChannelHandlerContext channelHandlerContext) {
        this.channel = creteChannelWrapper(channelHandlerContext);
    }

    private BridgenetChannel creteChannelWrapper(@NotNull ChannelHandlerContext channelHandlerContext) {
        return new BridgenetChannel(channelHandlerContext);
    }
}