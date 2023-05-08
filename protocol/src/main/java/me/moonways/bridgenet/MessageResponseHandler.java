package me.moonways.bridgenet;

import org.jetbrains.annotations.NotNull;

public class MessageResponseHandler {

    private final ResponseContainer responseContainer = new ResponseContainer();

    private int responseId = 0;

    public void handleResponse(int responseId, @NotNull Message message) {
        responseContainer.getResponse(responseId).complete(message);
    }

    public void addResponse(int responseId, @NotNull MessageResponse<?> messageResponse) {
        responseContainer.addResponse(responseId, messageResponse);
    }

    public int getNextAwaitResponseMessageId() {
        if (responseId == Integer.MAX_VALUE) {
            responseId = 0;
        }

        return responseId++;
    }
}