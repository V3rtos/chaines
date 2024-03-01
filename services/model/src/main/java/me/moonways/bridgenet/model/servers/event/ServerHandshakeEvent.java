package me.moonways.bridgenet.model.servers.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.model.servers.EntityServer;

@Getter
@ToString
@RequiredArgsConstructor
public class ServerHandshakeEvent implements Event {

    private final Handshake handshake;
    private final EntityServer server;
}
