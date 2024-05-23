package me.moonways.bridgenet.model.players.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.players.Player;
import me.moonways.bridgenet.model.servers.EntityServer;

@Getter
@Builder
@ToString
public class PlayerRedirectEvent {

    private final Player player;
    private final EntityServer server;
}
