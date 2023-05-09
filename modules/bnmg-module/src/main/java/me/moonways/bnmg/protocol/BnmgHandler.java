package me.moonways.bnmg.protocol;

import me.moonways.bnmg.descriptor.GuiDescriptor;
import me.moonways.bridgenet.protocol.message.BridgenetMessageHandler;
import me.moonways.bridgenet.protocol.message.MessageContainer;

public class BnmgHandler extends BridgenetMessageHandler {

    public BnmgHandler(MessageContainer messageContainer) {
        super(messageContainer);

        addHandler(BnmgCreateMessage.class, msg -> {

            GuiDescriptor descriptor = msg.getDescriptor();
        });
    }
}
