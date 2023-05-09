package me.moonways.bridgenet.protocol.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.exception.ResponseMessageNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class MessageResponseContainer {

    private final Map<Integer, MessageResponse<?>> responseMessagesMap = new HashMap<>();

    private int responseId = 0;

    private void validateNull(MessageResponse<?> response) {
        if (response == null) {
            throw new ResponseMessageNotFoundException("response message is not found");
        }
    }

    public void handleResponse(int responseId, @NotNull Message message) {
        MessageResponse<Message> response = getResponse(responseId);
        validateNull(response);

        response.complete(message);
    }

    public int getNextAwaitResponseMessageId() {
        if (responseId == Integer.MAX_VALUE) {
            responseId = 0;
        }

        return responseId++;
    }

    public void addResponse(int id, @NotNull MessageResponse<?> response) {
        responseMessagesMap.put(id, response);
    }

    public void removeResponse(int responseId) {
        responseMessagesMap.remove(responseId);
    }

    @SuppressWarnings("unchecked")
    public <M extends Message> MessageResponse<M> getResponse(int responseId) {
        return (MessageResponse<M>) Optional.ofNullable(responseMessagesMap.get(responseId))
                .orElseThrow(() -> new ResponseMessageNotFoundException(String.format("Can't find response message by id %s", responseId)));
    }
}
