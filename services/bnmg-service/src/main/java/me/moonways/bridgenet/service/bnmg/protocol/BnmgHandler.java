package me.moonways.bridgenet.service.bnmg.protocol;

import me.moonways.bridgenet.protocol.message.BridgenetMessageHandler;
import me.moonways.bridgenet.protocol.message.MessageContainer;
import me.moonways.bridgenet.service.bnmg.descriptor.GuiDescriptor;

public class BnmgHandler extends BridgenetMessageHandler {

    public BnmgHandler(MessageContainer messageContainer) {
        super(messageContainer);

        addHandler(BnmgCreateMessage.class, msg -> {

            GuiDescriptor descriptor = msg.getDescriptor();
        });
    }
}
