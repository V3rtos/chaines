package me.moonways.bridgenet.model.servers.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.servers.EntityServer;

@Getter
@ToString
@RequiredArgsConstructor
public class ServerDisconnectEvent implements Event {

    public enum DownstreamType {
        DOWNSTREAM,
        DISCONNECT_MESSAGE,
    }

    private final DownstreamType downstreamType;
    private final EntityServer server;
}
