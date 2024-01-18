package me.moonways.bridgenet.mtp.pipeline.response;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.persistence.MessageHandler;
import me.moonways.bridgenet.mtp.message.persistence.MessageTrigger;

@MessageHandler
public class DefaultMessageResponseHandler {

    @Inject
    private DefaultMessageResponseService service;

    @MessageTrigger
    public void handle(Object message) {
        if (service.isWaiting(message.getClass())) {
            service.complete(message);
        }
    }
}
