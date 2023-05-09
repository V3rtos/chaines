package me.moonways.bridgenet.protocol.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.protocol.exception.MessageNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class BridgenetMessageHandler {

    private final Map<Integer, Consumer<Message>> handleMessageMap = new HashMap<>();

    @Getter
    private final MessageContainer messageContainer;

    @SuppressWarnings("unchecked")
    public <M extends Message> void addHandler(Class<M> messageClass, Consumer<M> consumer) {
        int messageId = getMessageId(messageClass);
        handleMessageMap.put(messageId, (Consumer<Message>) consumer);
    }

    public void handle(int messageId, Message message) {
        Optional.ofNullable(handleMessageMap.get(messageId))
                .orElseThrow(() ->
                        new MessageNotFoundException(String.format("Message %s not found in handler", message.getClass().getName()))).accept(message);
    }

    private <M extends Message> int getMessageId(Class<M> messageClass) {
        return messageContainer.getIdByMessage(messageClass);
    }
}
