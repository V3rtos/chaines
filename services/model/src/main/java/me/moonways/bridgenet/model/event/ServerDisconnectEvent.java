package me.moonways.bridgenet.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.service.servers.EntityServer;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ServerDisconnectEvent implements Event {

    public enum DownstreamType {
        DOWNSTREAM,
        DISCONNECT_REQUEST,
    }

    private final DownstreamType downstreamType;
    private final EntityServer server;
}
