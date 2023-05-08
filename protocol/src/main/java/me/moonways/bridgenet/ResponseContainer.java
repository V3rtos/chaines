package me.moonways.bridgenet;

import me.moonways.bridgenet.exception.ResponseMessageNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResponseContainer {

    private final Map<Integer, MessageResponse<?>> responseMessagesMap = new HashMap<>();

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
