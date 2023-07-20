package me.moonways.bridgenet.bootstrap.hook.mtp;

import me.moonways.bridgenet.mtp.message.DefaultMessage;
import me.moonways.bridgenet.mtp.message.inject.MessageHandler;
import me.moonways.bridgenet.mtp.message.inject.MessageTrigger;

@MessageHandler
public class MTPHandler {

    @MessageTrigger
    public void handle(DefaultMessage defaultMessage) {
        System.out.println("handling");
    }
}
