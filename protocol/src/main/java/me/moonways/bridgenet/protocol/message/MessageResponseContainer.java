package me.moonways.bridgenet.protocol.message;

import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.exception.ResponseMessageNotFoundException;
import me.moonways.bridgenet.service.inject.Component;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
@NoArgsConstructor
@Component
public class MessageResponseContainer {

    private final Map<Integer, CompletableFuture<Message>> responseMessagesMap = new HashMap<>();

    private int responseId = 0;

    private void validateNull(CompletableFuture<Message> response) {
        if (response == null) {
            throw new ResponseMessageNotFoundException("response message is not found");
        }
    }

    public int nextIncrementedResponseID() {
        if (responseId == Integer.MAX_VALUE) {
            responseId = 1;
        }

        return ++responseId;
    }

    public void handleResponseFuture(int responseId, @NotNull Message message) {
        CompletableFuture<Message> response = getResponseHandler(responseId);
        validateNull(response);

        response.complete(message);
    }

    @SuppressWarnings("unchecked")
    public void saveResponseHandler(int id, @NotNull CompletableFuture<? extends Message> response) {
        responseMessagesMap.put(id, (CompletableFuture<Message>) response);
    }

    public void removeResponseHandler(int responseId) {
        responseMessagesMap.remove(responseId);
    }

    @SuppressWarnings("unchecked")
    public <M extends Message> CompletableFuture<M> getResponseHandler(int responseId) {
        return (CompletableFuture<M>) Optional.ofNullable(responseMessagesMap.get(responseId))
                .orElseThrow(() -> new ResponseMessageNotFoundException(String.format("Can't find response message by id %s", responseId)));
    }
}
