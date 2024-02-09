package me.moonways.bridgenet.mtp.message.response;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.persistence.IncomingMessageListener;
import me.moonways.bridgenet.mtp.message.persistence.Priority;
import me.moonways.bridgenet.mtp.message.persistence.SubscribeMessage;

@IncomingMessageListener(priority = Priority.LAST)
public class ResponsibleMessageHandler {

    @Inject
    private DefaultMessageResponseService service;

    @SubscribeMessage
    public void handle(Object message) {
        if (service.isWaiting(message.getClass())) {
            service.complete(message);
        }
    }
}
