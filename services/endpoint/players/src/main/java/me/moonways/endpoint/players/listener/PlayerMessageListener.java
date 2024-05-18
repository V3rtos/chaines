package me.moonways.endpoint.players.listener;

import me.moonways.bridgenet.model.bus.message.Disconnect;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.mtp.message.persistence.InboundMessageListener;
import me.moonways.bridgenet.mtp.message.persistence.SubscribeMessage;

@InboundMessageListener
public final class PlayerMessageListener {

    @SubscribeMessage
    public void handle(Handshake handshake) {
    }

    @SubscribeMessage
    public void handle(Disconnect disconnect) {
    }
}
