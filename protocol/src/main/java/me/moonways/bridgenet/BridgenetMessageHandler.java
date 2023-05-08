package me.moonways.bridgenet;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.exception.MessageNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class BridgenetMessageHandler {

    @Getter
    private final MessageResponseHandler responseHandler = new MessageResponseHandler();

    @Getter
    private final BridgenetChannelExecutor executor = new BridgenetChannelExecutor(responseHandler);

    @SuppressWarnings("rawtypes")
    private final Map<Integer, Consumer> handleMessageMap = new HashMap<>();

    private final MessageRegistryContainer messageRegistryContainer;

    public <M extends Message> void addHandler(Class<M> messageClass, Consumer<M> consumer) {
        int messageId = getMessageId(messageClass);

        handleMessageMap.put(messageId, consumer);
    }

    public void handleChannelActive(@NotNull ChannelHandlerContext channelHandlerContext) {
        executor.initializeChannelHandlerContext(channelHandlerContext);
    }

    @SuppressWarnings("unchecked")
    public void handle(int messageId, Message message) {
        Optional.ofNullable(handleMessageMap.get(messageId))
                .orElseThrow(() ->
                        new MessageNotFoundException(String.format("Message %s not found in handler", message.getClass().getName()))).accept(message);
    }

    private <M extends Message> int getMessageId(Class<M> messageClass) {
        return messageRegistryContainer.getIdByMessage(messageClass);
    }
}
