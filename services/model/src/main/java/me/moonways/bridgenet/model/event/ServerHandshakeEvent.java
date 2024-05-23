package me.moonways.bridgenet.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.message.Handshake;
import me.moonways.bridgenet.model.service.servers.EntityServer;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ServerHandshakeEvent implements Event {

    private final Handshake handshake;
    private final EntityServer server;
}
