package me.moonways.bridgenet.api.connection;

import me.moonways.bridgenet.protocol.message.MessageHandler;
import me.moonways.bridgenet.protocol.message.MessageTrigger;
import me.moonways.bridgenet.protocol.message.TestMessage;
import me.moonways.bridgenet.protocol.message.TestMessageResponse;

@MessageHandler
public class BridgenetHandler {

    @MessageTrigger
    public void handle(TestMessage testMessage) {
        System.out.println("qwerty");

        TestMessageResponse testMessageResponse = new TestMessageResponse(1, "Привет, респонс пришёл!");
        testMessageResponse.setResponseId(testMessage.getResponseId());

        testMessage.writeResponse(testMessageResponse);
    }
}
